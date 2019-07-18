package io.tradingservice.tradingservice.controllers;


import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.services.FXRateService;
import io.tradingservice.tradingservice.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/currency")
@Consumes("application/json")
@Produces("application/json")
public class FXRateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FXRateController.class);

    @Autowired
    private FXRateService fxRateService;

    @POST
    @Path(Constants.addCurrencyEndpoint)
    public Response addCurr(FXRate newFXRate) {
        LOGGER.info("Add currency end point accessed");
        return fxRateService.addCurrency(newFXRate);
    }

    @GET
    @Path(Constants.getCurrencyEndpoint)
    public Response getCurr(@QueryParam("currency") String currency){
        LOGGER.info("Get currency end point accessed");
        return fxRateService.getCurrencyResponse(currency);
    }

    @GET
    @Path(Constants.getAllCurrencyEndpoint)
    public Response getAllCurr(){
        LOGGER.info("Get all currencies end point accessed");
        return fxRateService.getAll();
    }

    @POST
    @Path(Constants.updateCurrencyEndpoint)
    public Response updateCurr(FXRate newFXRate){
        LOGGER.info("Update currency end point accessed");
        return fxRateService.updateCurrency(newFXRate);
    }
}
