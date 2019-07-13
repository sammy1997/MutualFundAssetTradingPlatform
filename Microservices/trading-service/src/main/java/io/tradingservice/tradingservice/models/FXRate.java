package io.tradingservice.tradingservice.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository(collection = "rates")
@JsonDeserialize(as = ImmutableFXRate.class)
@JsonSerialize(as = ImmutableFXRate.class)
public interface FXRate {
    @Mongo.Id
    String currency();
    float rate();
}
