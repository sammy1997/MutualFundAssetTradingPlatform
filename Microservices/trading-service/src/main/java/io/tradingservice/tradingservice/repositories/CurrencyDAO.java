package io.tradingservice.tradingservice.repositories;

import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.models.FXRateRepository;
import io.tradingservice.tradingservice.utils.Constants;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.List;

public class CurrencyDAO {
    private FXRateRepository repository;

    public CurrencyDAO(){
        repository = new FXRateRepository(RepositorySetup.forUri(Constants.mongoPort));
    }

    public void addCurrency(FXRate fxRate){
        repository.insert(fxRate);
    }

    public void updateCurrency(FXRate fxRate){
        repository.upsert(fxRate);
    }

    public Optional<FXRate> getCurrency(String currency){
        return repository.findByCurrency(currency).fetchFirst().getUnchecked();
    }

    public List<FXRate> getAll(){
        return repository.findAll().fetchAll().getUnchecked();
    }
}
