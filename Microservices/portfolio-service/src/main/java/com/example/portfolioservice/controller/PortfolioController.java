package com.example.portfolioservice.controller;


import com.example.portfolioservice.models.*;
import com.example.portfolioservice.service.PortfolioService;
import com.example.portfolioservice.utils.Constants;
import com.example.portfolioservice.utils.ServiceUtils;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/portfolio")
public class PortfolioController
{

    @Autowired
    private PortfolioService portfolioService;

    // Fetch user details
    @GET
    @Produces("application/json")
    @Path("/")
    public ImmutableUserDBModel getUserById(@HeaderParam("Authorization") String token)
    {
        return portfolioService.getUser(ServiceUtils.decodeJWTForUserId(token));
    }

    // Get user balance
    @GET
    @Produces("application/json")
    @Path("/getBalance")
    public float getBalanceById(@HeaderParam("Authorization") String token)
    {
        return portfolioService.getBalanceById(ServiceUtils.decodeJWTForUserId(token));

    }

    // Update user balance
    // Internal access
    @PATCH
    @Produces("application/json")
    @Path("/update")
    public void updateBalance(@HeaderParam("Authorization") String token, @QueryParam("balance") float balance,
                              @QueryParam("secret") String secret_key)
    {
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            portfolioService.updateBalance(ServiceUtils.decodeJWTForUserId(token), balance);
        }
    }


    // Delete a user
    // Can only be accessed by admin
    @DELETE
    @Produces("application/json")
    @Path("/delete/{userId}")
    public Optional<UserDBModel> deleteUserById(@PathParam("userId") String userId)
    {
        return portfolioService.delete(userId);
    }


    // Update user
    // Internal access
    @PATCH
    @Produces("application/json")
    @Path("/update/user")
    public Optional<UserDBModel> updateUserById(@QueryParam("secret") String secret_key, User2 user)
    {
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            if (user.all_funds().isPresent()){
                List<Fund2> currFunds = user.all_funds().get();
                List<Fund2> updateProfitsOfFunds = new ArrayList<>();
                for (Fund2 fund: currFunds){

                }
            }
            return portfolioService.update(user);
        }
        return null;
    }



    // Create user balance
    // Internal access
    @POST
    @Produces("application/json")
    @Path("/add/user/")
    public void addUser(@HeaderParam("Authorization") String token, @QueryParam("secret") String secret_key,
                        @QueryParam("balance") float balance)
    {
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            return;
        }
        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (portfolioService.getUser(userId) != null){
            return;
        }
        User2 user = ImmutableUser2.builder()
                .userId(userId)
                .all_funds(new ArrayList<>())
                .balance(balance)
                .build();
        portfolioService.createUser(user);
    }


    //get the funds of user. If user does not exist in DB add user.
    @GET
    @Produces("application/json")
    @Path("/getFunds/")
    //
    public List<Fund2> getFunds(@HeaderParam("Authorization") String token)
    {
        String userId = ServiceUtils.decodeJWTForUserId(token);

        ImmutableUserDBModel user = portfolioService.getUser(userId);

        return user.all_funds();

//            float f1 = 4.2f;
//            float profit = f1-(fund.originalNav().get());
//            float profitPercent = ((profit)/(fund.originalNav().get())) * 100;
//            Fund2 fun2 = ImmutableFund2.builder()
//                    .fundNumber(fund.fundNumber())
//                    .fundName(fund.fundName())
//                    .invManager(fund.invManager())
//                    .originalNav(fund.originalNav())
//                    .sAndPRating(fund.sAndPRating())
//                    .moodysRating(fund.moodysRating())
//                    .invCurrency(fund.invCurrency())
//                    .setCycle(fund.setCycle())
//                    .presentNav((float)(4.2))
//                    .profitAmount(profit)
//                    .profitPercent(profitPercent)
//                    .build();
//            newFunds.add(fun2);
//            return fun2;
//
//        }).collect(Collectors.toList());
//
//        User2 user = ImmutableUser2.builder()
//                    .userId(userId)
//                    .all_funds(newFunds)
//                    .balance((float)(10000))
//                    .build();
//
//        portfolioService.createUser(user);
//
//        return user.all_funds().get();
    }

}
