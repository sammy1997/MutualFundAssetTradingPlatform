package io.tradingservice.tradingservice.controllers;


import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.services.FXRateService;
import io.tradingservice.tradingservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/currency")
@Consumes("application/json")
@Produces("application/json")
public class FXRateController {

    @Autowired
    private FXRateService fxRateService;

    @POST
    @Path(Constants.addCurrencyEndpoint)
    public Response addCurr(FXRate newFXRate) {
        return fxRateService.addCurrency(newFXRate);
    }

    @GET
    @Path(Constants.getCurrencyEndpoint)
    public Response getCurr(@QueryParam("currency") String currency){
        return fxRateService.getCurrencyResponse(currency);
    }

    @GET
    @Path(Constants.getAllCurrencyEndpoint)
    public Response getAllCurr(){
        return fxRateService.getAll();
    }

    @POST
    @Path(Constants.updateCurrencyEndpoint)
    public Response updateCurr(FXRate newFXRate){
        return fxRateService.updateCurrency(newFXRate);
    }
}
