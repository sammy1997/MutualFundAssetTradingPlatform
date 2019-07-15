package io.tradingservice.tradingservice.models;

import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.sql.Timestamp;

@Value.Immutable
@Mongo.Repository
public interface Transaction {

    @Mongo.Id
    String fundNumber();
    String fundName();
    float avgNav();
    String status();
    float quantity();
    String invManager();
    String setCycle();
    String invCurr();
    float sAndPRating();
    float moodysRating();
    Timestamp time();
}
