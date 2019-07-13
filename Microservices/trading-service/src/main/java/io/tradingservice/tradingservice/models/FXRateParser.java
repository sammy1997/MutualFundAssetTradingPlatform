package io.tradingservice.tradingservice.models;

import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Mongo.Repository
public interface FXRateParser {

    List<ImmutableFXRate> fxRates();
}
