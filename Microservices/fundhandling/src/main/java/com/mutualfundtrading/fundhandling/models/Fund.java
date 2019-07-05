package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

@Value.Immutable
@Mongo.Repository(collection = "Funds")
@JsonSerialize(as = ImmutableFund.class)
@JsonDeserialize(as = ImmutableFund.class)
public interface Fund {
    @Mongo.Id
    String fundNumber();
    String fundName();
    String invManager();
    int setCycle();
    float nav();
    String invCurrency();
    float sAndPRating();
    float moodysRating();

}
