package io.tradingservice.tradingservice.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.ImmutableUser;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.User;
import io.tradingservice.tradingservice.services.UserTradeService;
import io.tradingservice.tradingservice.utils.ServiceUtils;
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
    @Path("/exchange")
    public Response exchangeTrade(@HeaderParam("Authorization") String header, Trade trade) throws URISyntaxException {
        // return userTradeService.purchaseTrade(userId, trade);
        String userId = ServiceUtils.decodeJWTForUserId(header);
        int res = userTradeService.exchangeTrade(userId, trade);
        if (res == 1) return Response.status(201).entity("Purchased requested trade").build();
        else if (res == -1) return Response.status(200).entity("Sold requested trade").build();
        else if (res == -5) return Response.status(400).entity("Insufficient funds to sell").build();
        else return Response.status(400).entity("Bad request").build();
    }

    // View Trades Api
    @GET
    @Path("/view")
    public List<ImmutableTrade> getAllTrades(@HeaderParam("Authorization") String header) {
        String userId = ServiceUtils.decodeJWTForUserId(header);
        List<ImmutableTrade> trades = userTradeService.getAllTrades(userId);
        return trades;
    }

    // Add List of Trades
    @POST
    @Path("/purchase")
    public Response purchaseTrade(@HeaderParam("Authorization") String header, List<Trade> trades) {
        if (trades.size() <= 5) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            int res = userTradeService.purchaseTrade(userId, trades, header);
            return Response.status(201).entity("Purchased requested Funds").build();
        } else return Response.status(400).entity("Bad request").build();
    }
}