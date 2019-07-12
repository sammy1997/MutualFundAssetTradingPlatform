package io.tradingservice.tradingservice.services;

import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.repositories.FXRateAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
public class FXRateService {

    @Autowired
    private FXRateAccessObject fxRateAccessObject;

    // Function to add the new FX Rate
    public Response addFXRate(FXRate newFXRate) {
        if (fxRateAccessObject.addFXRate(newFXRate) == 1) {
            return Response.status(Response.Status.CREATED)
                    .entity("Added FX Rate for " + newFXRate.currency())
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    }

    // Function to remove the FX Rate
    public Response removeFXRate(String currency) {
        if (fxRateAccessObject.removeFXRate(currency)) {
            return Response.status(Response.Status.OK)
                    .entity("Removed FX Rate for " + currency)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    }

    public Response updateFXRate(FXRate newFXRate) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    }

    public FXRate getFXRateBYCurrency(String currency) {
        return fxRateAccessObject.getFXRate(currency);
    }

    public Response getFXRatesByCurrencies(List<String> currencies) {
        try {
            List<FXRate> fxRates = fxRateAccessObject.getFXRatesByIds(currencies);
            return Response.status(Response.Status.OK).entity(fxRates).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    }

    public Response getAllFXRates() {
        try {
            List<FXRate> fxRates = fxRateAccessObject.getAllFXRates();
            return Response.status(Response.Status.OK).entity(fxRates).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
    }

}
