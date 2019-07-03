package io.tradingservice.tradingservice.repositories;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.utils.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.immutables.mongo.Mongo;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

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

    // Helper function to directly add fund to corresponding userId
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

    // Create a new user
    public boolean addUser(String userId){
        if (!userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
            User newUser =
                    ImmutableUser.builder()
                            .userId(userId)
                            .trades(new ArrayList<>())
                            .build();
            userRepository.insert(newUser);
            return true;
        } else return false;
    }

    // Helper function for conversion rate
    private float getConversionRate(String baseCurr, String tradeCurr){
        float convRate = Constants.FX_USD.get(baseCurr)/ Constants.FX_USD.get(tradeCurr);
        return convRate;
    }

    // To get list of Trades of given user(userId)
    public List<ImmutableTrade> getAllTradesByUserId(String userId){
        boolean isPresent = userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent();
        if (isPresent) {
            return userRepository.findByUserId(userId).fetchFirst().getUnchecked().get().trades();
        } return null;
    }

    // Verify Trades
    public boolean verify(String userId, List<Trade> newTrades, float balance, String baseCurr){
        int count = 0;
        if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
            User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
            List<ImmutableTrade> currTrades = user.trades();
            for (Trade t: newTrades){
                if (t.status().equals("purchase")){
                    float debit = t.quantity() * t.avgNav() * getConversionRate(baseCurr, t.invCurr());
                    balance -= debit;
                    count++;
                }
                if (t.status().equals("sell")){
                    for (ImmutableTrade tradeExist: currTrades){
                        if (tradeExist.fundNumber().equals(t.fundNumber()) && tradeExist.quantity() >= t.quantity()){
                            float credit = t.quantity() * t.avgNav() * getConversionRate(baseCurr, t.invCurr());
                            balance += credit;
                            count++;
                            break;
                        }
                    }
                }
            }
            if (count == newTrades.size() && balance >= 0){
                return true;
            } else return false;
        } else return false;
    }

    // Update an existing fund
    public float updateFund(String userId, Trade trade, String fundId, float balance, String baseCurr){
        // If status is purchase
        if (trade.status().equals("purchase")) {
            float debit;
            if (baseCurr!=trade.invCurr()){
                float convRate = getConversionRate(baseCurr, trade.invCurr());
                debit = trade.avgNav() * trade.quantity() * convRate;
            } else debit = trade.avgNav() * trade.quantity();
            if (debit > balance) return 0;
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
                return -debit;
            }
        } else if (trade.status().equals("sell")){           // If the status is sell
            float credit;
            if (baseCurr!=trade.invCurr()){
                float convRate = getConversionRate(baseCurr, trade.invCurr());
                credit = trade.avgNav() * trade.quantity() * convRate;
            } else credit = trade.avgNav() * trade.quantity();
            if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
                User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
                List<ImmutableTrade> trades = user.trades();
                ImmutableTrade t = getFundById(trades, fundId);
                // If the quantity is zero, remove trade directly
                if (trade.quantity()==t.quantity()){
                    directRemoveFund(userId, fundId);
                    credit = trade.quantity()*trade.avgNav();
                    return credit;
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
                    return credit;
                } else return 0;   // Bad request wherein sell quantity strictly greater than existing
            }
        } return 0;
    }

    // Condition checks for adding a trade
    public float addTrade(String userId, Trade trade, float balance, String baseCurr){
        if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
            User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
            List<ImmutableTrade> trades = user.trades();
            String fundId = trade.fundNumber();
            int count = 0;
            for (ImmutableTrade t: trades){
                // Fund already exists
                if (t.fundNumber().equals(fundId) /*&& currBalance >= trade.quantity()*trade.avgNav()*/){
                    return updateFund(userId, trade, fundId, balance, baseCurr);
                }
                count++;
            }
            // Fund doesn't exist
            if (count==trades.size()){
                directAddFund(userId, trade);
                float debit = -trade.quantity()*trade.avgNav();
                return debit;
            }
        }
        return 0;
    }

}