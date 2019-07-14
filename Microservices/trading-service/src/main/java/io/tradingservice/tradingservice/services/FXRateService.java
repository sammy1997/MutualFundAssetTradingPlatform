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

    public Response addCurrency(FXRate parser){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(parser.currency());
        if (fxRateOptional.isPresent()){
            return Response.status(422).entity("Currency already present").build();
        }
        FXRate fxRate = ImmutableFXRate.builder()
                .currency(parser.currency()).rate(parser.rate())
                .build();

        fxRateAccessObject.addCurrency(fxRate);
        return Response.status(200).entity("Currency added").build();
    }

    public Response updateCurrency(FXRate newFXRate){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(newFXRate.currency());
        if (!fxRateOptional.isPresent()){
            return Response.status(422).entity("Currency not present in DB").build();
        }
        FXRate fxRate = ImmutableFXRate.builder()
                .currency(newFXRate.currency()).rate(newFXRate.rate())
                .build();

        fxRateAccessObject.updateCurrency(fxRate);
        return Response.status(200).entity("Currency updated").build();
    }

    public FXRate getCurrency(String currency){
        Optional<FXRate> fxRateOptional = fxRateAccessObject.getCurrency(currency);
        if (fxRateOptional.isPresent()){
            return fxRateOptional.get();
        }
        return null;
    }

    public List<FXRate> getAll(){
        return fxRateAccessObject.getAll();
    }
}
