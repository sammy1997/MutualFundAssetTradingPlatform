package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;


@Value.Immutable
@JsonSerialize(as = ImmutableFund.class)
@JsonDeserialize(as = ImmutableFund.class)
public interface Fund {
    String fundNumber();
    Optional<String> fundName();
    Optional<String> invManager();
    Optional<Integer> setCycle();
    Optional<Float> nav();
    Optional<String> invCurrency();
    Optional<Float> sAndPRating();
    Optional<Float> moodysRating();
}

