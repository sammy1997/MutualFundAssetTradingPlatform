package io.tradingservice.tradingservice.controllers;


import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.services.FXRateService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rates")
@Consumes("application/json")
@Produces("application/json")
public class FXRateController {

    @Autowired
    private FXRateService fxRateService;

    @POST
    @Path("/add")
    public Response addFXRate(FXRate newFXRate){
        return fxRateService.addFXRate(newFXRate);
    }

    @POST
    @Path("/delete")
    public Response deleteFXRate(String currency){
        return fxRateService.removeFXRate(currency);
    }

    @POST
    @Path("/update")
    public Response updateFXRate(FXRate newFXRate){
        return fxRateService.updateFXRate(newFXRate);
    }

    @POST
    @Path("/get")
    public Response getFXRatesByCurrencies(List<String> currencies){
        return fxRateService.getFXRatesByCurrencies(currencies);
    }

    @POST
    @Path("/getAll")
    public Response getAllFXRates(){
        return fxRateService.getAllFXRates();
    }

}
