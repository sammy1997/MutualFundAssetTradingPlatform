package com.example.portfolioservice.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

@SuppressWarnings("ALL")
@Value.Immutable
@JsonSerialize(as = ImmutableFundParser.class)
@JsonDeserialize(as = ImmutableFundParser.class)
public interface FundParser {
    String fundNumber();
    Optional<String> fundName();
    Optional<String> invManager();
    Optional<Integer> setCycle();
    Optional<Float> nav();
    Optional<String> invCurrency();
    Optional<Float> sAndPRating();
    Optional<Float> moodysRating();
}
