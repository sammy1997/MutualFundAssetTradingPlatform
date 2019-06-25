package io.tradingservice.tradingservice.repositories;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.*;
import org.immutables.mongo.Mongo;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public class UserAccessObject {

    // Create instance of user repository
    UserRepository userRepository;

    // Constructor of dao when called
    public UserAccessObject() {
        userRepository = new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/UserTrades"));
    }

    // Helper function to get specific fund info based on the userId and fundId(fundNumber)
    private ImmutableTrade getFundById(List<ImmutableTrade> trades, String fundId){
        for (ImmutableTrade t: trades) {
            if (t.fundNumber().equals(fundId)) return t;
        }
        return null;
    }

    // Helper function to directly add fund
    private void directAddFund(String userId, Trade trade){
        ImmutableTrade t = ImmutableTrade.builder().from(trade).build();
        userRepository.findByUserId(userId)
                .andModifyFirst()
                .addTrades(t)
                .upsert();
    }

    // Helper function to Remove fund only if exists
    private void directRemoveFund(String userId, String fundId){
        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
        for (ImmutableTrade t: user.trades()){
            if (t.fundNumber().equals(fundId)){
                userRepository.findByUserId(userId)
                        .andModifyFirst()
                        .removeTrades(t)
                        .upsert();
                break;
            }
        }
    }

    // Helper function to Create a new user
    public int addUser(String userId){
        User newUser =
                ImmutableUser.builder()
                        .userId(userId)
                        .trades(new ArrayList<>())
                        .build();
        userRepository.insert(newUser);
        if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()) return 1;
        else return 0;
    }


    // To get list of Trades of given user(userId)
    public List<ImmutableTrade> getAllTradesByUserId(String userId){
        boolean isPresent = userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent();
        if (isPresent) {
            return userRepository.findByUserId(userId).fetchFirst().getUnchecked().get().trades();
        } return null;
    }

    // Update an existing fund
    public int updateFund(String userId, Trade trade, String fundId){
        // If status is purchase
        if (trade.status().equals("purchase")) {
            // Trade already exists, so no need for non existence case
            if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()) {
                User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
                List<ImmutableTrade> trades = user.trades();
                ImmutableTrade t = getFundById(trades, fundId);
                float newQuantity = t.quantity() + trade.quantity();
                float newAvgNav = (t.quantity() * t.avgNav() + trade.quantity() * trade.avgNav()) / newQuantity;
                ImmutableTrade newT =
                        ImmutableTrade.builder()
                                .fundNumber(fundId)
                                .fundName(trade.fundName())
                                .avgNav(newAvgNav)
                                .status(trade.status())
                                .quantity(newQuantity)
                                .invManager(trade.invManager())
                                .setCycle(trade.setCycle())
                                .invCurr(trade.invCurr())
                                .sAndPRating(trade.sAndPRating())
                                .moodysRating(trade.moodysRating())
                                .build();
                userRepository.findByUserId(userId).andModifyFirst()
                        .addTrades(newT).upsert();
                userRepository.findByUserId(userId).andModifyFirst()
                        .removeTrades(t).upsert();
                // currBal = currBal - trade.quantity()*trade.avgNav();
                return 1;
            }
        } else if (trade.status().equals("sell")){           // If the status is sell
            if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
                User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
                List<ImmutableTrade> trades = user.trades();
                ImmutableTrade t = getFundById(trades, fundId);
                // If the quantity is zero, remove trade directly
                if (trade.quantity()==t.quantity()){
                    directRemoveFund(userId, fundId);
                    // currBal = currBal + trade.quantity()*trade.avgNav();
                    return -1;
                }
                else if (trade.quantity()<t.quantity()){        // Condition that sell quantity strictly less than existent
                    float newQuantity = t.quantity() - trade.quantity();
                    float newAvgNav = (t.quantity()*t.avgNav() - trade.quantity()*trade.avgNav())/ newQuantity;
                    ImmutableTrade newT =
                            ImmutableTrade.builder()
                                    .fundNumber(fundId)
                                    .fundName(trade.fundName())
                                    .avgNav(newAvgNav)
                                    .status(trade.status())
                                    .quantity(newQuantity)
                                    .invManager(trade.invManager())
                                    .setCycle(trade.setCycle())
                                    .invCurr(trade.invCurr())
                                    .sAndPRating(trade.sAndPRating())
                                    .moodysRating(trade.moodysRating())
                                    .build();
                    userRepository.findByUserId(userId).andModifyFirst()
                            .removeTrades(t).upsert();
                    userRepository.findByUserId(userId).andModifyFirst()
                            .addTrades(newT).upsert();
                    // currBal = currBal + trade.quantity()*trade.avgNav();
                    return -1;
                } else return -5;   // Bad request wherein sell quantity strictly greater than existing
            }
        } return 0;
    }

    // Condition checks for adding a trade
    public int addTrade(String userId, Trade trade){
        boolean exists = userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent();
        if (!exists){
            addUser(userId);
            directAddFund(userId, trade);
            return 1;
        }
        if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
            User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
            List<ImmutableTrade> trades = user.trades();
            String fundId = trade.fundNumber();
            int count = 0;
            for (ImmutableTrade t: trades){
                // Fund already exists
                if (t.fundNumber().equals(fundId) /*&& currBalance >= trade.quantity()*trade.avgNav()*/){
                    return updateFund(userId, trade, fundId);
                }
                count++;
            }
            // Fund doesn't exist
            if (count==trades.size()){
                directAddFund(userId, trade);
                return 1;
            }
        }
        return -5;
    }


}



//    private boolean findTradeById(List<ImmutableTrade> trades, String fundId){
//        for (Trade t: trades ){
//            if (t.fundNumber().equals(fundId)) return true;
//            else return false;
//        }
//        return false;
//    }





//    public List<User> getUsers(){
//        List<User> outUsers = new ArrayList<>();
//        List<User> users = userRepository.findAll().fetchAll().getUnchecked();
//        for (User user: users){
//            outUsers.add(user);
//        }
//        return users;
//    }


//    public User getUser(String userId){
//        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
//        return user;
//    }
