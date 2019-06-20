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

    // Purchase/Sell trade Api
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

    // View Trades Api
    @GET
    @Path("/view/{userId}")
    public List<ImmutableTrade> getAllTrades(@PathParam("userId") String userId){
        List<ImmutableTrade> trades = userTradeService.getAllTrades(userId);
        return trades;
    }

}
