package io.tradingservice.tradingservice.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                                        // Service annotation
@JsonSerialize(as = ImmutableTrade.class)       // Serialisation
@JsonDeserialize(as = ImmutableTrade.class)     //      and deserialization
public class UserTradeService {

    // Create an instance of DAO
    UserAccessObject userAccessObject = new UserAccessObject();

    // To make a trade exchange method call by user(userId) and the requested trade(trade)
    public int exchangeTrade(String userId, Trade trade){
        return  userAccessObject.addTrade(userId, trade);
    }

    // To call method(of the dao) to view all trades of given user by userId
    public List<ImmutableTrade> getAllTrades(String userId){
        return userAccessObject.getAllTradesByUserId(userId);
    }

}
