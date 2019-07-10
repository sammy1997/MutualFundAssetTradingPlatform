package com.example.portfolioservice.controller;

import com.example.portfolioservice.models.User;
import com.example.portfolioservice.models.UserParser;
import com.example.portfolioservice.models.Fund;
import com.example.portfolioservice.models.FundParser;
import com.example.portfolioservice.models.BalanceInfo;
import com.example.portfolioservice.models.ImmutableFund;
import com.example.portfolioservice.models.ImmutableUserParser;
import com.example.portfolioservice.service.PortfolioService;
import com.example.portfolioservice.utils.Constants;
import com.example.portfolioservice.utils.ServiceUtils;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PATCH;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Component
@Path("/")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private WebClient client;

    // Fetch user details
    @GET
    @Produces("application/json")
    @Path("/")
    public User getUserById(@HeaderParam("Authorization") String token) {
        return portfolioService.getUser(ServiceUtils.decodeJWTForUserId(token)).get();
    }

    // Get user balance
    @GET
    @Produces("application/json")
    @Path("/getBalance")
    public BalanceInfo getBalanceById(@HeaderParam("Authorization") String token) {
        return portfolioService.getBalanceById(ServiceUtils.decodeJWTForUserId(token)).get();
    }

    // Update user balance
    // Internal access
    @PATCH
    @Produces("application/json")
    @Path("/update")
    public Response updateBalance(@HeaderParam("Authorization") String token,
                                  @QueryParam("balance") float balance,
                                  @QueryParam("secret") String secret_key) {
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
    public Optional<User> deleteUserById(@PathParam("userId") String userId) {
        return portfolioService.delete(userId);
    }


    // Update userParser
    // Internal access
    @PATCH
    @Produces("application/json")
    @Path("/update/user")
    public Response updateUserById(@HeaderParam("Authorization") String token,
                                                @QueryParam("secret") String secret_key,
                                                UserParser userParser) {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (Constants.SECRET_TOKEN.equals(secret_key)){
            if (userParser.all_funds().isPresent() && userId != null) {
                List<Fund> currFunds = userParser.all_funds().get();
                List<Fund> updateProfitsOfFunds = new ArrayList<>();
                for (Fund fund: currFunds) {
                    ClientResponse response =
                            client.get()
                            .uri("http://localhost:8762/fund-handling/api/entitlements/get/fund?fundNumber="
                                    + fund.fundNumber())
                            .header("Authorization", "Bearer " + token)
                            .exchange()
                            .block();

                    FundParser parsedFund = response.bodyToMono(FundParser.class).block();
                    if (parsedFund != null) {
                        float profit = (parsedFund.nav().get()
                                        - fund.originalNav().get()) * fund.quantity().get();
                        float profitPercent = (parsedFund.nav().get() - fund.originalNav().get())
                                               / fund.originalNav().get();
                        Fund updatedFund = ImmutableFund.builder().fundNumber(fund.fundNumber())
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
                userParser = ImmutableUserParser.builder()
                             .userId(userId).currBal(userParser.currBal())
                             .all_funds(updateProfitsOfFunds).build();
                portfolioService.update(userParser);
                return Response.status(200).entity("Updated profile successfully").build();
            }

            return Response.status(400).entity("Invalid request body").build();
        }
        return Response.status(400).entity("Wrong secret key").build();
    }

    //update user Base Currency
    @POST
    @Produces("application/json")
    @Path("/update/baseCurrency")
    public String updateBaseCurrency(@HeaderParam("Authorization") String token,
                                     @QueryParam("Currency") String newCurrency)
    {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        return portfolioService.updateBaseCurrency(userId, newCurrency);
    }

    // Create user balance
    // Internal access
    @POST
    @Produces("application/json")
    @Path("/add/user")
    public Response addUser(@HeaderParam("Authorization") String token,
                            @QueryParam("secret") String secret_key,
                            @QueryParam("balance") float balance,
                            @QueryParam("baseCurr") String baseCurr) {

        if (!Constants.SECRET_TOKEN.equals(secret_key)){
            return Response.status(400).entity("Secret key not correct").build();
        }

        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (portfolioService.getUser(userId).isPresent()){
            return Response.status(400).entity("UserParser already exists").build();
        }

        UserParser userParser = ImmutableUserParser.builder()
                .userId(userId)
                .baseCurr(baseCurr)
                .all_funds(new ArrayList<>())
                .currBal(balance)
                .build();
        portfolioService.createUser(userParser);
        return Response.status(200).entity("UserParser added to DB").build();
    }


    // Get the funds of user.
    @GET
    @Produces("application/json")
    @Path("/getFunds/")
    public List<Fund> getFunds(@HeaderParam("Authorization") String token) {
        String userId = ServiceUtils.decodeJWTForUserId(token);
        Optional<User> user = portfolioService.getUser(userId);
        if (user.isPresent())
            return user.get().all_funds();
        List<Fund> emptyFunds = new ArrayList<>();
        return emptyFunds;
    }

}
