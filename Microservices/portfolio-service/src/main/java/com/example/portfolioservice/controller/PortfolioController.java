package com.example.portfolioservice.controller;


import com.example.portfolioservice.models.*;
import com.example.portfolioservice.service.PortfolioService;
import com.example.portfolioservice.utils.Constants;
import com.example.portfolioservice.utils.ServiceUtils;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/")
public class PortfolioController
{

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private WebClient client;

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
    public BalanceInfo getBalanceById(@HeaderParam("Authorization") String token)
    {
        return portfolioService.getBalanceById(ServiceUtils.decodeJWTForUserId(token));
    }

    // Update user balance
    // Internal access
    @PATCH
    @Produces("application/json")
    @Path("/update")
    public Response updateBalance(@HeaderParam("Authorization") String token, @QueryParam("balance") float balance,
                              @QueryParam("secret") String secret_key)
    {
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            portfolioService.updateBalance(ServiceUtils.decodeJWTForUserId(token), balance);
            return Response.status(200).entity("Balance updated").build();
        }
        return Response.status(400).entity("Secret key is wrong").build();
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
    public Response updateUserById(@HeaderParam("Authorization") String token,
                                                @QueryParam("secret") String secret_key, User2 user)
    {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            if (user.all_funds().isPresent() && userId!=null){
                List<Fund2> currFunds = user.all_funds().get();
                List<Fund2> updateProfitsOfFunds = new ArrayList<>();
                for (Fund2 fund: currFunds){
                    ClientResponse response =
                            client.get()
                            .uri("http://localhost:8762/fund-handling/api/entitlements/get/fund?fundNumber=" + fund.fundNumber())
                            .header("Authorization", "Bearer " + token)
                            .exchange()
                            .block();

                    FundParser parsedFund = response.bodyToMono(FundParser.class).block();

                    if (parsedFund!=null){
                        float profit = (parsedFund.nav().get() - fund.originalNav().get())*fund.quantity().get();
                        float profitPercent = (parsedFund.nav().get() - fund.originalNav().get())/fund.originalNav().get();
                        Fund2 updatedFund = ImmutableFund2.builder().fundNumber(fund.fundNumber())
                                .fundName(parsedFund.fundName()).invCurrency(parsedFund.invCurrency())
                                .invManager(parsedFund.invManager())
                                .moodysRating(parsedFund.moodysRating())
                                .originalNav(fund.originalNav())
                                .presentNav(parsedFund.nav())
                                .quantity(fund.quantity())
                                .sAndPRating(parsedFund.sAndPRating())
                                .setCycle(parsedFund.setCycle())
                                .profitAmount(profit).profitPercent(profitPercent)
                                .build();
                        updateProfitsOfFunds.add(updatedFund);
                    }
                }
                user = ImmutableUser2.builder().userId(userId).currBal(user.currBal())
                        .all_funds(updateProfitsOfFunds).build();
                portfolioService.update(user);
                return Response.status(200).entity("Updated profile successfully").build();
            }

            return Response.status(400).entity("Invalid request body").build();
        }
        return Response.status(400).entity("Wrong secret key").build();
    }



    // Create user balance
    // Internal access
    @POST
    @Produces("application/json")
    @Path("/add/user/")
    public Response addUser(@HeaderParam("Authorization") String token, @QueryParam("secret") String secret_key,
                            @QueryParam("balance") float balance, @QueryParam("baseCurr") String baseCurr)
    {
        if (!Constants.SECRET_TOKEN.equals(secret_key)){
            return Response.status(400).entity("Secret key not correct").build();
        }

        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (portfolioService.getUser(userId) != null){
            return Response.status(400).entity("User already exists").build();
        }

        User2 user = ImmutableUser2.builder()
                .userId(userId)
                .baseCurr(baseCurr)
                .all_funds(new ArrayList<>())
                .currBal(balance)
                .build();
        portfolioService.createUser(user);
        return Response.status(200).entity("User added to DB").build();
    }


    // Get the funds of user. If user does not exist in DB add user.
    @GET
    @Produces("application/json")
    @Path("/getFunds/")
    public List<Fund2> getFunds(@HeaderParam("Authorization") String token)
    {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        ImmutableUserDBModel user = portfolioService.getUser(userId);
        if (user==null)
            return null;
        return user.all_funds();
    }

}
