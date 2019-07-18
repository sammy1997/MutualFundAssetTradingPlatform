package io.tradingservice.tradingservice.repositories;

import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.services.FXRateService;
import io.tradingservice.tradingservice.utils.Constants;
import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;



public class UserAccessObject {

    // Create instance of user repository
    private UserRepository userRepository;

    @Autowired
    private FXRateService fxRateService;

    // Constructor of dao when called
    public UserAccessObject() {
        userRepository = new UserRepository(RepositorySetup.forUri(Constants.mongoPort));
    }


    // Helper function to get specific fund info based on the userId and fundId(fundNumber)
    private ImmutableTrade getFundById(List<ImmutableTrade> trades, String fundId){

        // Loop through existing user trades
        for (ImmutableTrade t: trades) {

            // Find the fund trade
            if (t.fundNumber().equals(fundId)) return t;
        }
        return ImmutableTrade.builder().build();
    }


    // Helper function to directly add fund to corresponding userId
    private void directAddFund(String userId, Trade trade){

        // Create the immutable instance and append
        ImmutableTrade t = ImmutableTrade.builder().from(trade).build();
        FluentFuture<Optional<User>> addition = userRepository.findByUserId(userId)
            .andModifyFirst()
            .addTrades(t)
            .upsert();
    }


    // Helper function to Remove fund only if exists
    private void directRemoveFund(String userId, String fundId){

        // Drop trade instance from directory
        User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
        for (ImmutableTrade t: user.trades()){

            // Find the trade
            if (t.fundNumber().equals(fundId)){

                // Drop the funds
                FluentFuture<Optional<User>> x =userRepository.findByUserId(userId)
                        .andModifyFirst()
                        .removeTrades(t)
                        .upsert();
                break;
            }
        }
    }


    // Helper function for conversion rate
    private float getConversionRate(String baseCurr, String tradeCurr){

        float convRate = 0;
        float baseRate = fxRateService.getCurrency(baseCurr).rate();
        float tradeRate = fxRateService.getCurrency(tradeCurr).rate();

        // Calculate the conversion rate ratio
        if (baseRate!=0 && tradeRate!=0) convRate = baseRate / tradeRate;
        return convRate;

    }


    // Create a new user
    public boolean addUser(String userId){

        // Create an immutable instance of a user
        if (!userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){

            // Create and insert the new document
            User newUser =
                    ImmutableUser.builder()
                            .userId(userId)
                            .trades(new ArrayList<>())
                            .build();
            userRepository.insert(newUser);
            return true;
        } else return false;
    }


    // To get list of Trades of given user(userId)
    public List<ImmutableTrade> getAllTradesByUserId(String userId){

        // Get the user and find his trades
        boolean isPresent = userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent();
        if (isPresent) {
            return userRepository.findByUserId(userId).fetchFirst().getUnchecked().get().trades();
        } return new ArrayList<>();
    }

    // Verify Trades
    public int verify(String userId, List<Trade> newTrades, float balance, String baseCurr) {

        // Count is used to make sure all trades are verified
        int count = 0;

        // Find user and calculate balance (if the trades are to be carried out)
        if (userRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()){
            User user = userRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
            List<ImmutableTrade> currTrades = user.trades();

            for (Trade t: newTrades) {

                // To ensure valid presence of Currencies
                float convRate = getConversionRate(baseCurr, t.invCurr());
                if (convRate == 0) {
                    System.out.println("One (or more) currencies does not exist in database");
                    return -4;
                }


                // If the trade status is set to purchase
                if (t.status().equals("purchase")) {

                    // Calculate new balance after the trade
                    float debit = t.quantity() * t.avgNav() * convRate;
                    System.out.println(t.quantity());
                    balance -= debit;
                    count++;
                    System.out.println("HERE" + balance);
                }

                // If the trade status is set to sell
                if (t.status().equals("sell")) {

                    // Loop through existing assets
                    for (ImmutableTrade tradeExist: currTrades) {

                        // Find the fund through existing assets
                        if (tradeExist.fundNumber().equals(t.fundNumber())) {
                            if (tradeExist.quantity() >= t.quantity()) {
                                // Calculate new balance after the trade
                                if (t.setCycle().equals("T")) {
                                    float credit = t.quantity() * t.avgNav() * convRate;
                                    balance += credit;
                                }
                                count++;
                                break;
                            }
                            else return -5;
                        }
                    }
                }
            }
            System.out.print("Count = " + count);
            System.out.println("Size = " + newTrades.size());
            System.out.println("Balance = " + balance);

            if (balance < 0) return -1;

            // If all trades are carried out and positive balance
            if (count == newTrades.size() && balance >= 0){
                System.out.println("Trades verified");
                return 1;
            } else {
                System.out.println("Not veri");
                return -2;
            }
        } else {
            System.out.println("not verified");
            return 0;
        }
    }


    // Update an existing trade of a fund
    public float updateFund(String userId, Trade trade, String fundId, float balance, String baseCurr){

        // If status is purchase
        if (trade.status().equals("purchase")) {

            // Debit is to be calculated
            float debit;
            if (baseCurr!=trade.invCurr()){

                // Calculate conversion
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
                FluentFuture<Optional<User>> addition = userRepository.findByUserId(userId).andModifyFirst()
                        .addTrades(newT).upsert();
                FluentFuture<Optional<User>> removal = userRepository.findByUserId(userId).andModifyFirst()
                        .removeTrades(t).upsert();
                if(addition.isDone() && removal.isDone()) return -debit;
            }
        }

        // If status is sell
        else if (trade.status().equals("sell")){
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
                    return credit;
                }
                else if (trade.quantity()<t.quantity()){        // Condition that sell quantity strictly less than existent
                    float newQuantity = t.quantity() - trade.quantity();
//                    float newAvgNav = (t.quantity()*t.avgNav() - trade.quantity()*trade.avgNav())/ newQuantity;
                    ImmutableTrade newT =
                            ImmutableTrade.builder()
                                    .fundNumber(fundId)
                                    .fundName(trade.fundName())
                                    .avgNav(t.avgNav())
                                    .status(trade.status())
                                    .quantity(newQuantity)
                                    .invManager(trade.invManager())
                                    .setCycle(trade.setCycle())
                                    .invCurr(trade.invCurr())
                                    .sAndPRating(trade.sAndPRating())
                                    .moodysRating(trade.moodysRating())
                                    .build();
                    FluentFuture<Optional<User>> removal = userRepository.findByUserId(userId).andModifyFirst()
                            .removeTrades(t).upsert();
                    FluentFuture<Optional<User>> addition = userRepository.findByUserId(userId).andModifyFirst()
                            .addTrades(newT).upsert();
                    if(removal.isDone() && addition.isDone()) return credit;
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
                float convRate = getConversionRate(baseCurr, trade.invCurr());
                float debit = trade.quantity() * trade.avgNav() * convRate;
                directAddFund(userId, trade);
                return -debit;
            }
        }
        return 0;
    }

}