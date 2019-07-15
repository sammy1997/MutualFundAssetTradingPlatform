package io.tradingservice.tradingservice.models;


import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Mongo.Repository(collection = "transactions")
public interface UserTransaction {

    @Mongo.Id
    String userId();
    List<Transaction> transactions();

}
