package io.tradingservice.tradingservice.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
//import org.springframework.beans.factory.annotation.Autowired;
import io.tradingservice.tradingservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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

    // To make a purchase method call by user(userId) and the requested trade(trade)
    // public int exchangeTrade(String userId, Trade trade){
        // return  userAccessObject.addTrade(userId, trade);
    //}

    // To call method(of the dao) to view all trades of given user by userId
    public List<ImmutableTrade> getAllTrades(String userId){
        return userAccessObject.getAllTradesByUserId(userId);
    }

    public int purchaseTrade(String userId, List<Trade> trades, String header) {
        float balance = webClientBuilder.build()
                .get()
                .uri("http://portfolio-service/{view}" )
                .retrieve()
                .bodyToMono(float.class)
                .block();
        for (Trade t: trades ){
            balance += userAccessObject.addTrade(userId, t);
        }
        List<ImmutableTrade> updatedTrades = getAllTrades(userId);
        User2 user2 =
                ImmutableUser2.builder()
                        .userId(userId)
                        .balance(balance)
                        .trades(updatedTrades)
                        .build();
        webClientBuilder.build()
                .post()
                .uri("http://portfolio-service/{update}?secret=" + Constants.SECRET_KEY)
                .body(BodyInserters.fromObject(user2))
                .header(header);
        return 1;
    }
}
