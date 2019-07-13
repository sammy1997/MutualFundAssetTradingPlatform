package io.tradingservice.tradingservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableFXRateParser.class)
@JsonDeserialize(as = ImmutableFXRateParser.class)
public interface FXRateParser {
    String currency();
    float rate();
}
