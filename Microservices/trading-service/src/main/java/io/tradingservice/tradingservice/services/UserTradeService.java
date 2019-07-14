package io.tradingservice.tradingservice.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
//import org.springframework.beans.factory.annotation.Autowired;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service                                        // Service annotation
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
                .setCycle("None")
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
                if (!oldFund.setCycle().equals("None")){
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
    private int verifyTrades(String userId, List<Trade> trades, String header){

        // Verify Possibility of trades
        BalanceInfo balanceInfo = getBalance(header);
        float balance = balanceInfo.getCurrBal();
        System.out.println(balance);
        String baseCurr = balanceInfo.getBaseCurr();
        System.out.println(baseCurr);
        int exchangePossible = userAccessObject.verify(userId, trades, balance, baseCurr);
        System.out.println(exchangePossible);

        switch (exchangePossible)
        {
            case 0:
                return 0;
            case -1:
                return -1;
            case -2:
                return -2;
        }

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

        if(!isEntitled) return -3;
        // Set verification status
        if (exchangePossible == 1){
            return 1;
        } else return 0;
    }

    // For purchasing / selling trades
    private float exchangeTrade(String userId, List<Trade> trades, String header) {

        // Trade happens only after verification
        if (verifyTrades(userId, trades, header) == 1) {

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

    public Response exchangeResponse(String header, List<TradeParser> tradeParsers) {
        if (tradeParsers.size() <= 5) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            List<Trade> trades = makeTrades(tradeParsers, header);
            System.out.println(trades);
            if (trades.isEmpty()) return Response.status(Response.Status.OK).entity("No trades requested").build();
            float res = exchangeTrade(userId, trades, header);
            if (res == (float)-1) return Response.status(Response.Status.BAD_REQUEST).entity("Trades not verified").build();
            else if (res == (float)-2) return Response.status(Response.Status.BAD_REQUEST).entity("Enter non zero quantity").build();
            else {
                System.out.println(res);
                updateUser(userId, header, res);
                return Response.status(Response.Status.CREATED).entity("Exchanged Requested trade").build();
            }
        } else return Response.status(Response.Status.BAD_REQUEST).entity("Only upto 5 trades can be placed at once").build();
    }

    public Response verifyResponse(String header, List<TradeParser> tradeParsers) {
        String userId = ServiceUtils.decodeJWTForUserId(header);
        List<Trade> trades = makeTrades(tradeParsers, header);
        if (trades.isEmpty()) return Response.status(Response.Status.OK).entity("No trades are requested").build();
        int isVerified = verifyTrades(userId, trades, header);
        switch (isVerified){
            case 1:
                return Response.status(Response.Status.OK).entity("Trades are verified").build();
            case -1:
                return Response.status(Response.Status.OK).entity("Trades not verified: Insufficient balance").build();
            case -2:
                return Response.status(Response.Status.OK).entity("Trades not verified: Cannot do all trades today").build();
            case -3:
                return Response.status(Response.Status.OK).entity("Trades not verified: Not entitled to one or more fund(s)").build();
            case -4:
                return Response.status(Response.Status.OK).entity("Trades not verified: Currency does not exist").build();
            default:
                return Response.status(Response.Status.OK).entity("Trades not verified: Unknown Error").build();
        }
    }
}
