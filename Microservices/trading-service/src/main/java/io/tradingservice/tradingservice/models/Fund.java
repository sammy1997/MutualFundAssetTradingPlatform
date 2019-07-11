package io.tradingservice.tradingservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableFund.class)
@JsonDeserialize(as = ImmutableFund.class)
public interface Fund {

    String fundNumber();
    String fundName();
    String invManager();
    String setCycle();
    float nav();
    String invCurrency();
    float sAndPRating();
    float moodysRating();
}
