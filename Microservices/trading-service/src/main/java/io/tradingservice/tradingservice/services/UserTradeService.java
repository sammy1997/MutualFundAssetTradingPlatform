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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import static com.sun.tools.doclint.Entity.trade;

@Service                                        // Service annotation
@JsonSerialize(as = ImmutableTrade.class)       // Serialisation
@JsonDeserialize(as = ImmutableTrade.class)     //      and deserialization
public class UserTradeService {

    // Create an instance of DAO
    UserAccessObject userAccessObject = new UserAccessObject();

    // Create instance of Webclient
    @Autowired
    WebClient.Builder webClientBuilder;

    // Helper function to check entitlements
    private boolean isEntitled(List<ImmutableFund> entitlements, Trade t){
        for (ImmutableFund fund: entitlements){
            if (t.fundNumber().equals(fund.fundNumber())) return true;
        }
        return false;
    }

    // To call method(of the dao) to view all trades of given user by userId
    public List<ImmutableTrade> getAllTrades(String userId){
        return userAccessObject.getAllTradesByUserId(userId);
    }

    // For purchasing / selling trades
    public int exchangeTrade(String userId, List<Trade> trades, String header) {

        // Get current balance
        BalanceInfo balanceInfo = webClientBuilder.build()
                .get()
                .uri("http://localhost:8762/portfolio/getBalance" )
                .header("Authorization", "Bearer " + header)
                .retrieve()
                .bodyToMono(BalanceInfo.class)
                .block();

        float balance = balanceInfo.getBalance();
        String baseCurr = balanceInfo.getBaseCurr();

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


        // Add trades
        for (Trade t: trades ){
            if (isEntitled(entitlements, t)) balance += userAccessObject.addTrade(userId, t, balance, baseCurr);
        }

        // Create the funds for sending update request
        List<ImmutableTrade> updatedTrades = getAllTrades(userId);
        List<FundParser> funds = new ArrayList<>();
        for (ImmutableTrade t: updatedTrades){
            FundParser fund = new FundParser();
            fund.setFundNumber(t.fundNumber());
            fund.setOriginalNav(t.avgNav());
            fund.setQuantity((int) t.quantity());        // We are only considering integral quantities for now
            funds.add(fund);
        }

        // Create the updated user
        User2 updatedUser = ImmutableUser2.builder()
                            .userId(userId)
                            .balance(balance)
                            .funds(funds)
                            .build();


        // Update the balance 2
        webClientBuilder.build()
                .patch()
                .uri("http://loachost:8762/portfolio/update/user?secret" + Constants.SECRET_KEY)
                .header("Authorization", "Bearer " + header)
                .body(BodyInserters.fromObject(updatedUser))
                .exchange()
                .block();

        return 1;
    }

}
