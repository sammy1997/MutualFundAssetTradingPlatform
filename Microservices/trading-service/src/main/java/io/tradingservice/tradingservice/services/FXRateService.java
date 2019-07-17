package io.tradingservice.tradingservice.services;


import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.models.ImmutableFXRate;
import io.tradingservice.tradingservice.repositories.FXRateAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;


@Service
public class FXRateService {

    @Autowired
    private FXRateAccessObject fxRateAccessObject;

    public Response addCurrency(FXRate newFXRate){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(newFXRate.currency());
        if (fxRateOptional.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST).entity("Currency already present").build();
        }
        FXRate fxRate = ImmutableFXRate.builder()
                .currency(newFXRate.currency()).rate(newFXRate.rate())
                .build();

        fxRateAccessObject.addCurrency(fxRate);
        return Response.status(Response.Status.OK).entity("Currency added").build();
    }

    public Response updateCurrency(FXRate newFXRate){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(newFXRate.currency());
        if (!fxRateOptional.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST).entity("Currency not present in DB").build();
        }
        FXRate fxRate = ImmutableFXRate.builder()
                .currency(newFXRate.currency()).rate(newFXRate.rate())
                .build();

        fxRateAccessObject.updateCurrency(fxRate);
        return Response.status(Response.Status.OK).entity("Currency updated").build();
    }

    public FXRate getCurrency(String currency){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(currency);
        if (fxRateOptional.isPresent()){
            return fxRateOptional.get();
        }
        return ImmutableFXRate.builder()
                    .currency("None")
                    .rate(0)
                    .build();
    }

    public List<FXRate> getAll(){
        List<FXRate> fxRates = fxRateAccessObject.getAll();
        return fxRates;
    }

    public Response getCurrencyResponse(String currency) {
        FXRate fxRate = getCurrency(currency);
        if (fxRate.currency().equals("None")) {
            return Response.status(Response.Status.OK).entity("Currency does not exist").build();
        }

        return Response.status(Response.Status.OK).entity(fxRate).build();
    }
}
