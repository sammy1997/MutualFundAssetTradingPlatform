package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository(collection = "rates")
@JsonSerialize(as = ImmutableFXRate.class)
@JsonDeserialize(as = ImmutableFXRate.class)
public interface FXRate {
    @Mongo.Id
    String currency();
    float rate();
}