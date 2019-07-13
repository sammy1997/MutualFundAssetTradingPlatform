package io.tradingservice.tradingservice.models;


import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository(collection = "rates")
public interface FXRate {

    @Mongo.Id
    String currency();
    float rate();
}
