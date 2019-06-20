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
    UserRepository userRepository;

    public UserAccessObject() {
        userRepository = new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/UserTrades"));
    }


    private boolean findTradeById(List<ImmutableTrade> trades, String fundId){
        for (Trade t: trades ){
            if (t.fundNumber().equals(fundId)) return true;
            else return false;
        }
        return false;
    }

    private ImmutableTrade getFundById(List<ImmutableTrade> trades, String fundId){
        int count = 0;
        for (ImmutableTrade t: trades) {
            if (t.fundNumber().equals(fundId)) return t;
            count++;
        }
        return null;
    }

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


    public List<User> getUsers(){
        List<User> outUsers = new ArrayList<>();
        List<User> users = userRepository.findAll().fetchAll().getUnchecked();
        for (User user: users){
            outUsers.add(user);
        }
        return users;
    }

    public User getUser(String userId){
        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
        return user;
    }

    public void directAddFund(String userId, Trade trade){
        ImmutableTrade t = ImmutableTrade.builder().from(trade).build();
        userRepository.findByUserId(userId)
                .andModifyFirst()
                .addTrades(t)
                .upsert();
    }

    public void directRemoveFund(String userId, String fundId){
        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
        for (ImmutableTrade t: user.trades()){
            if (t.fundNumber().equals(fundId)){
                userRepository.findByUserId(userId).andModifyFirst()
                        .removeTrades(t)
                        .upsert();
                break;
            }
        }
    }

    public int updateFund(String userId, Trade trade, String fundId){
        if (trade.status().equals("purchase")) {
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
                return 1;
            }
        }else if (trade.status().equals("sell")){
            if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
                User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
                List<ImmutableTrade> trades = user.trades();
                ImmutableTrade t = getFundById(trades, fundId);
                if (trade.quantity()<=t.quantity()){
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
                    return -1;
                } else {return -5; }

            }
        }

        return 0;
    }

    public int addTrade(String userId, Trade trade){
        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
        List<ImmutableTrade> trades = user.trades();
        String fundId = trade.fundNumber();
        int count = 0;
        for (ImmutableTrade t: trades){
            if (t.fundNumber().equals(fundId)){
                return updateFund(userId, trade, fundId);

            }
            count++;
        }
        if (count==trades.size()){
            directAddFund(userId, trade);
            return 1;
        }

        return -5;
    }

}
