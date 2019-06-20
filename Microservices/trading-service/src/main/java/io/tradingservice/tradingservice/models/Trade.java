package io.tradingservice.tradingservice.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.security.Timestamp;

@Value.Immutable
@JsonSerialize(as = ImmutableTrade.class)
@JsonDeserialize(as = ImmutableTrade.class)
@Mongo.Repository
public interface Trade {

    @Mongo.Id
    String fundNumber();
    String fundName();
    //Timestamp timeStamp();
    float avgNav();
    String status();
    float quantity();
    String invManager();
    int setCycle();
    String invCurr();
    float sAndPRating();
    float moodysRating();




}
