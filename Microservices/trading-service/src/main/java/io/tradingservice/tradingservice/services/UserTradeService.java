package io.tradingservice.tradingservice.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@JsonSerialize(as = ImmutableTrade.class)
@JsonDeserialize(as = ImmutableTrade.class)
public class UserTradeService {


    UserAccessObject userAccessObject = new UserAccessObject();

    public int purchaseTrade(String userId, Trade trade){

         return  userAccessObject.addTrade(userId, trade);
    }

    public void addTradeDirectly(String userId, Trade trade){
        userAccessObject.directAddFund(userId, trade);
    }

    public void removeTradeDirectly(String userId, String fundId){
        userAccessObject.directRemoveFund(userId, fundId);
    }

//    public boolean createUser(String userId){
//        if (userAccessObject.addUser(userId)==1){
//            return true;
//        } else {return false;}
//    }

    public boolean createUser(String userId){
        if (userAccessObject.addUser(userId)==1) return true;
        else return false;
    }

    public List<User> getAllUsers(){
        return userAccessObject.getUsers();
    }

    public User getUserById(String userId){
        return userAccessObject.getUser(userId);
    }

}
