package io.tradingservice.tradingservice.repositories;

import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.models.FXRateRepository;
import io.tradingservice.tradingservice.utils.Constants;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;

public class FXRateAccessObject {

    private FXRateRepository fxRateRepository;

    public FXRateAccessObject(){
        fxRateRepository = new FXRateRepository(RepositorySetup.forUri(Constants.mongoPort));
    }

    public void addCurrency(FXRate fxRate){
        fxRateRepository.insert(fxRate);
    }

    public void updateCurrency(FXRate fxRate){
        fxRateRepository.upsert(fxRate);
    }

    public Optional<FXRate> getCurrency(String currency){
        return fxRateRepository.findByCurrency(currency).fetchFirst().getUnchecked();
    }

    public List<FXRate> getAll(){
        if (fxRateRepository.findAll().fetchAll().getUnchecked().isEmpty()){
            return new ArrayList<>();
        }
        return fxRateRepository.findAll().fetchAll().getUnchecked();
    }

}
