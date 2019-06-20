package io.tradingservice.tradingservice.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.ImmutableUser;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.User;
import io.tradingservice.tradingservice.services.UserTradeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URISyntaxException;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/")
@Consumes("application/json")
@Produces("application/json")
@JsonSerialize(as = ImmutableTrade.class)
@JsonDeserialize(as = ImmutableTrade.class)
public class TradeServiceController {

    @Autowired
    UserTradeService userTradeService;

    @POST
    @Path("/purchase/{userId}")
    public Response purchaseTrade(@PathParam("userId") String userId, Trade trade) throws URISyntaxException {
        // return userTradeService.purchaseTrade(userId, trade);
        int res = userTradeService.purchaseTrade(userId, trade);
        if (res==1) return Response.status(201).entity("Purchased requested trade").build();
        else if (res==-1) return Response.status(200).entity("Sold requested trade").build();
        else if (res==-5) return Response.status(400).entity("Insufficient funds to sell").build();
        else return Response.status(400).entity("Bad request").build();
    }

    @POST
    @Path("/add/{userId}")
    public Response addTradeDirectly(@PathParam("userId") String userId, Trade trade) throws URISyntaxException {
        userTradeService.addTradeDirectly(userId, trade);
        return Response.status(201).entity("Added Trade").build();
    }

    @POST
    @Path("/create/{userId}")
    public Response createUser(@PathParam("userId") String userId) throws URISyntaxException {
        if (userTradeService.createUser(userId)){
            return Response.status(201).entity("New user created: "+ userId).build();
        } else return Response.status(400).entity("Bad request").build();
    }

    @GET
    @Path("/users/all")
    public List<User> getAllUsers(){
        List<User> users = userTradeService.getAllUsers();
        return users;
    }

    @GET
    @Path("/users/{userId}")
    public User getUserById(@PathParam("userId") String userId){
        return userTradeService.getUserById(userId);
    }

    @DELETE
    @Path("remove/{userId}")
    public Response removeTradeDirectly(@PathParam("userId") String userId, String fundId){
        userTradeService.removeTradeDirectly(userId, fundId);
        return Response.status(200).entity("Removed " + fundId).build();
    }

}
