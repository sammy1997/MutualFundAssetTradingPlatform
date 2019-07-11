package io.tradingservice.tradingservice.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
//import org.springframework.beans.factory.annotation.Autowired;
import io.tradingservice.tradingservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

//import static com.sun.tools.doclint.Entity.trade;

@Service                                        // Service annotation
@JsonSerialize(as = ImmutableTrade.class)       // Serialisation
@JsonDeserialize(as = ImmutableTrade.class)     //      and deserialization
public class UserTradeService {

    // Create an instance of DAO
    @Autowired
    private UserAccessObject userAccessObject;

    // Create instance of Webclient
    @Autowired
    private WebClient.Builder webClientBuilder;

    // Helper function to check entitlements
    private boolean isEntitled(List<ImmutableFund> entitlements, Trade t){

        for (ImmutableFund fund: entitlements){
            if (t.fundNumber().equals(fund.fundNumber())) return true;
        }
        return false;
    }

    // Helper function to find the fund details
    private ImmutableFund existFunds(List<ImmutableFund> entitlements, TradeParser tradeParser){
        for (ImmutableFund fund: entitlements){
            if (fund.fundNumber().equals(tradeParser.getFundNumber())){
                return fund;
            }
        }
        return ImmutableFund.builder()
                .fundNumber("None")
                .fundName("None")
                .invManager("None")
                .nav(-1)
                .invCurrency("None")
                .setCycle(-1)
                .sAndPRating(-1)
                .moodysRating(-1)
                .build();
    }

    // Helper function to get balance
    private BalanceInfo getBalance(String header){

        // Get current balance
        BalanceInfo balanceInfo = webClientBuilder.build()
                .get()
                .uri("http://localhost:8762/portfolio/getBalance" )
                .header("Authorization", "Bearer " + header)
                .retrieve()
                .bodyToMono(BalanceInfo.class)
                .block();

        return balanceInfo;
    }

    // Helper function to get entitlements
    private List<ImmutableFund> getEntitlements(String header){

        // Get entitlements
        String response = (webClientBuilder.build()
                .get()
                .uri("http://localhost:8762/fund-handling/api/entitlements/get")
                .header("Authorization", "Bearer " + header)
                .retrieve()
                .bodyToMono(String.class)
                .block());
        System.out.println(response);
        response = "{\"entitlements\":" + response + "}";
        Gson gson = new Gson();
        List<ImmutableFund> entitlements = gson.fromJson(response, ListParser.class).getEntitlements();
        return entitlements;
    }

    // To call method(of the dao) to view all trades of given user by userId
    public List<ImmutableTrade> getAllTrades(String userId){
        return userAccessObject.getAllTradesByUserId(userId);
    }

    // For creating a new user
    public boolean addUserById(String userId){
        return userAccessObject.addUser(userId);
    }

    // To make the trades
    public List<Trade> makeTrades(List<TradeParser> tradeParsers, String header){

        List<ImmutableFund> entitlements = getEntitlements(header);
        List<Trade> trades = new ArrayList<>();
        for (TradeParser t: tradeParsers) {
            ImmutableFund oldFund = existFunds(entitlements, t);
                if (oldFund.setCycle()!=-1){
                    Trade newTrade = ImmutableTrade.builder().fundNumber(oldFund.fundNumber())
                                                .fundName(oldFund.fundName())
                                                .avgNav(oldFund.nav())
                                                .status(t.getStatus())
                                                .quantity(t.getQuantity())
                                                .invManager(oldFund.invManager())
                                                .setCycle(oldFund.setCycle())
                                                .invCurr(oldFund.invCurrency())
                                                .sAndPRating(oldFund.sAndPRating())
                                                .moodysRating(oldFund.moodysRating())
                                                .build();
                    trades.add(newTrade);
            } else {
                return new ArrayList<>();
            }
        }
        return trades;
    }

    // For verifying the trades
    public boolean verifyTrades(String userId, List<Trade> trades, String header){

        // Verify Possibility of trades
        BalanceInfo balanceInfo = getBalance(header);
        float balance = balanceInfo.getCurrBal();
        System.out.println(balance);
        String baseCurr = balanceInfo.getBaseCurr();
        System.out.println(baseCurr);
        boolean exchangePossible = userAccessObject.verify(userId, trades, balance, baseCurr);
        System.out.println(exchangePossible);

        // Verify Entitlements
        List<ImmutableFund> entitlements = getEntitlements(header);
        int count = 0;
        for (Trade t: trades){
            if (isEntitled(entitlements,t)){
                count++;
            }
        }
        boolean isEntitled = false;
        if (count == trades.size()) {
            isEntitled = true;
        }
        System.out.println(isEntitled);

        // Set verification status
        if (exchangePossible && isEntitled){
            return true;
        }
        else return false;
    }

    // For purchasing / selling trades
    public float exchangeTrade(String userId, List<Trade> trades, String header) {

        // Trade happens only after verification
        if (verifyTrades(userId, trades, header)) {

            // Get balance and base Currency of the user
            BalanceInfo balanceInfo = getBalance(header);
            float balance = balanceInfo.getCurrBal();
            String baseCurr = balanceInfo.getBaseCurr();

            // Add trades
            for (Trade t : trades) {
                if (t.quantity()==0) return -2;
                balance += userAccessObject.addTrade(userId, t, balance, baseCurr);
            }
            System.out.println("YOUR BALANCE HERE IS " + balance);
            // Create the funds for sending update request
            // updateUser(userId, header, balance);
            return balance;
        } else return -1;
    }

    public void updateUser(String userId, String header, float newBalance){

        // Delay to complete the asynchronous call
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

        // Create the updated attributes of funds' list
        List<ImmutableTrade> updatedTrades = getAllTrades(userId);
        System.out.println(updatedTrades);

        List<FundParser> funds = new ArrayList<>();
        for (ImmutableTrade t : updatedTrades) {
            FundParser fund = new FundParser();
            fund.setFundNumber(t.fundNumber());
            fund.setOriginalNav(t.avgNav());
            fund.setQuantity((int) t.quantity());        // We are only considering integral quantities for now
            funds.add(fund);
        }

        // Create the updated user
        UserParser updatedUser = ImmutableUserParser.builder()
                .userId(userId)
                .currBal(newBalance)
                .all_funds(funds)
                .build();

        System.out.println(updatedUser);

        // Update the balance
        ClientResponse response2 = webClientBuilder.build()
                .patch()
                .uri("http://localhost:8762/portfolio/update/user?secret=" + Constants.SECRET_KEY)
                .header("Authorization", "Bearer " + header)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(updatedUser))
                .exchange()
                .block();

        System.out.println(response2.statusCode().value());
    }


    /////////////////////////////////////////////////  END OF SERVICE  /////////////////////////////////////////////
}
