package io.tradingservice.tradingservice.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.services.UserTradeService;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.text.html.parser.Entity;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    // View Trades Api
    @GET
    @Path("/view")
    public List<ImmutableTrade> getAllTrades(@HeaderParam("Authorization") String header) {
        String userId = ServiceUtils.decodeJWTForUserId(header);
        List<ImmutableTrade> trades = userTradeService.getAllTrades(userId);
        return trades;
    }

    // Purchase & sell trades
    @POST
    @Path("/exchange")
    public Response exchangeTrade(@HeaderParam("Authorization") String header, List<Trade> trades) {
        if (trades.size() <= 5) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            float res = userTradeService.exchangeTrade(userId, trades, header);
            if (res == 0) return Response.status(400).entity("Trades not verified").build();
            else {
                userTradeService.updateUser(userId, header, res);
                return Response.status(400).entity("Exchanged Requested trade").build();
            }
        } else return Response.status(400).entity("Max trade request is 5").build();
    }

    // Verify trades
    @POST
    @Path("/verify")
    public Response verifyTrades(@HeaderParam("Authorization") String header, List<Trade> trades){
        String userId = ServiceUtils.decodeJWTForUserId(header);
        boolean isVerified = userTradeService.verifyTrades(userId, trades, header);
        if (isVerified) return Response.status(200).entity("Verified Trades").build();
        else return Response.status(200).entity("Not verified: Trades not possible").build();
    }


}