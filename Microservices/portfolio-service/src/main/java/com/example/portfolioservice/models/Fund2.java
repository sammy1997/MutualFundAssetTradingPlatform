package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.Optional;

@Mongo.Repository
@Value.Immutable
@JsonSerialize(as = ImmutableFund2.class)
@JsonDeserialize(as = ImmutableFund2.class)
public interface Fund2 {
    String fundNumber();
    Optional<String> fundName();
    Optional<String> invManager();
    Optional<Integer> setCycle();
    Optional<Float> presentNav();
    Optional<Float> originalNav();
    Optional<String> invCurrency();
    Optional<Float> sAndPRating();
    Optional<Float> moodysRating();
    Optional<Float> profitAmount();
    Optional<Float> profitPercent();
}

