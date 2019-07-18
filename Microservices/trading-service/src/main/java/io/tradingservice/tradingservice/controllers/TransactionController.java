package io.tradingservice.tradingservice.controllers;

import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.services.TransactionService;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes("application/json")
@Produces("application/json")

public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    //Transactions history Api
    @GET
    @Path(Constants.transactionHistoryEndPoint)
    public Response getAllTransactions(@HeaderParam("Authorization") String header) {
        LOGGER.info("Get all transactions end point accessed");
        String userId = ServiceUtils.decodeJWTForUserId(header);
        return transactionService.getAllTransactionsById(userId);
    }

    //Add transactions Api
    @POST
    @Path(Constants.addTransactionEndPoint)
    public Response addTransactions(@HeaderParam("Authorization") String header, Trade newTrade) {
        LOGGER.info("Add transaction end point accessed");
        String userId = ServiceUtils.decodeJWTForUserId(header);
        return transactionService.addTransactionById(userId, newTrade);
    }
}