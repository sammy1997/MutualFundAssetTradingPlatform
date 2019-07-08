package io.tradingservice.tradingservice.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.TradeParser;
import io.tradingservice.tradingservice.services.UserTradeService;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
    private UserTradeService userTradeService;

    // View Trades Api
    @GET
    @Path("/view")
    public List<ImmutableTrade> getAllTrades(@HeaderParam("Authorization") String header) {
        String userId = ServiceUtils.decodeJWTForUserId(header);
        List<ImmutableTrade> trades = userTradeService.getAllTrades(userId);
        return trades;
    }

    // Exchange trades Api
    @POST
    @Path("/exchange")
    public Response exchangeTrade(@HeaderParam("Authorization") String header, List<TradeParser> tradeParsers) {
        if (tradeParsers.size() <= 5) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            List<Trade> trades = userTradeService.makeTrades(tradeParsers, header);
            System.out.println(trades);
            if (trades == null) return Response.status(400).entity("Trades not verified").build();
            float res = userTradeService.exchangeTrade(userId, trades, header);
            if (res == 0) return Response.status(400).entity("Trades not verified").build();
            else {
                System.out.println(res);
                userTradeService.updateUser(userId, header, res-1);
                return Response.status(201).entity("Exchanged Requested trade").build();
            }
        } else return Response.status(400).entity("Max trade request is 5").build();
    }

    // Verify trades Api
    @POST
    @Path("/verify")
    public  Response verifyTrades(@HeaderParam("Authorization") String header, List<TradeParser> tradeParsers){
        String userId = ServiceUtils.decodeJWTForUserId(header);
        List<Trade> trades = userTradeService.makeTrades(tradeParsers, header);
        if (trades == null) return Response.status(400).entity("Trades not verified").build();
        boolean isVerified = userTradeService.verifyTrades(userId, trades, header);
        if (isVerified) return Response.status(200).entity("Verified Trades").build();
        return Response.status(400).entity("Trades not verified").build();
    }

    // Add user
    @POST
    @Path("/addUser")
    public Response addUserById(@HeaderParam("Authorization") String header, @QueryParam("secret") String key) {
        if (key.equals(Constants.SECRET_KEY)) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            boolean success = userTradeService.addUserById(userId);
            if (success) return Response.status(201).entity("User Created")
                                                .build();
            else return Response.status(400).entity("Bad request").build();
        } else return Response.status(403).entity("Unauthorized request")
                                            .build();
    }


}