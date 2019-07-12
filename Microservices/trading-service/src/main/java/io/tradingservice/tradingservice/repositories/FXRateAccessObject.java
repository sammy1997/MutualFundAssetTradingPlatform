package io.tradingservice.tradingservice.repositories;

import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.FXRate;
import io.tradingservice.tradingservice.models.FXRateRepository;
import io.tradingservice.tradingservice.models.ImmutableFXRate;
import io.tradingservice.tradingservice.utils.Constants;
import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;

public class FXRateAccessObject {

    // FX Rates repository and criteria
    private FXRateRepository fxRateRepository;
    private FXRateRepository.Criteria where;

    // Constructor
    public FXRateAccessObject() {
        fxRateRepository = new FXRateRepository(RepositorySetup.forUri(Constants.mongoPort));
        where = fxRateRepository.criteria();
    }

    // For adding a new FX Rate
    public int addFXRate(FXRate fxRate){
        try {
            ImmutableFXRate newFXRate =
                    ImmutableFXRate.builder()
                            .from(fxRate).build();
            fxRateRepository.insert(newFXRate);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // For removing FX Rate
    public boolean removeFXRate(String currency) {
        Optional<FXRate> currFXRate = fxRateRepository.findByCurrency(currency).fetchFirst().getUnchecked();
        if (currFXRate.isPresent()) {
            fxRateRepository.find(where.currency(currency)).deleteAll();
            return true;
        } else return false;
    }

    // For updating an FX Rate
    public boolean updateFXRate(FXRate fxRate){
        if (fxRateRepository.findByCurrency(fxRate.currency()).fetchFirst().getUnchecked().isPresent()) {
            fxRateRepository.findByCurrency(fxRate.currency()).andModifyFirst().setRate(fxRate.rate()).upsert();
            return true;
        } else return false;
    }

    // For getting required FX Rates
    public List<FXRate> getFXRatesByIds(List<String> currencies) {
        List<FXRate> fxRates = new ArrayList<>();
        for (String currency: currencies) {
            try {
                FXRate fxRate = fxRateRepository.findByCurrency(currency).fetchFirst().getUnchecked().get();
                fxRates.add(fxRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fxRates;
    }

    // For getting all the FX Rates
    public List<FXRate> getAllFXRates() {
        try {
            return fxRateRepository.findAll().fetchAll().getUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public FXRate getFXRate(String currency) {
        try {
            return fxRateRepository.findByCurrency(currency).fetchFirst().getUnchecked().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ImmutableFXRate.builder()
                .currency("None")
                .rate(0)
                .build();
    }

}
