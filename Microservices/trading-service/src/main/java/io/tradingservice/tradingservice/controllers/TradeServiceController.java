package io.tradingservice.tradingservice.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.TradeParser;
import io.tradingservice.tradingservice.services.UserTradeService;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/")
@Consumes("application/json")
@Produces("application/json")
@JsonSerialize(as = ImmutableTrade.class)
@JsonDeserialize(as = ImmutableTrade.class)
public class TradeServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceController.class);

    @Autowired
    private UserTradeService userTradeService;

    // View Trades Api
    @GET
    @Path(Constants.viewEndPoint)
    public List<ImmutableTrade> getAllTrades(@HeaderParam("Authorization") String header) {
        LOGGER.info("Get all trades end point accessed");
        String userId = ServiceUtils.decodeJWTForUserId(header);
        return userTradeService.getAllTrades(userId);
    }

    // Exchange trades Api
    @POST
    @Path(Constants.exchangeEndPoint)
    public Response exchangeTrade(@HeaderParam("Authorization") String header, List<TradeParser> tradeParsers) {
        LOGGER.info("Exchange trade end point accessed");
        return userTradeService.exchangeResponse(header, tradeParsers);
    }

    // Verify trades Api
    @POST
    @Path(Constants.verifyEndPoint)
    public  Response verifyTrades(@HeaderParam("Authorization") String header, List<TradeParser> tradeParsers){
        LOGGER.info("Verify trades end point accessed");
        return userTradeService.verifyResponse(header, tradeParsers);
    }

    // Add user
    @POST
    @Path(Constants.addUserEndPoint)
    public Response addUserById(@HeaderParam("Authorization") String header, @QueryParam("secret") String key) {
        LOGGER.info("Add user end point accessed");
        if (key.equals(Constants.SECRET_KEY)) {
            String userId = ServiceUtils.decodeJWTForUserId(header);
            boolean success = userTradeService.addUserById(userId);
            if (success) return Response.status(Response.Status.CREATED).entity("User Created")
                                                .build();
            else return Response.status(Response.Status.BAD_REQUEST).entity("Bad request").build();
        } else return Response.status(Response.Status.FORBIDDEN).entity("Forbidden request")
                                            .build();
    }
}