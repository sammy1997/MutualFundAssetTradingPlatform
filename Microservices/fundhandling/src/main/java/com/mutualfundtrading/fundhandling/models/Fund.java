package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.mongo.types.Id;
import org.immutables.value.Value;
import org.immutables.value.internal.$processor$.meta.$MongoMirrors;

import javax.annotation.Generated;

@Value.Immutable
@Mongo.Repository(collection = "Funds")
@JsonSerialize(as = ImmutableFund.class)
@JsonDeserialize(as = ImmutableFund.class)
/*public abstract class Fund {
    @Mongo.Id
    public abstract String fundNumber();
    public abstract String fundName();
    public abstract String invManager();
    public abstract int setCycle();
    public abstract float nav();
    public abstract String invCurrency();
    public abstract float sAndPRating();
    public abstract float moodysRating();

}*/
public interface Fund {
    @Mongo.Id
    public abstract String fundNumber();
    public abstract String fundName();
    public abstract String invManager();
    public abstract int setCycle();
    public abstract float nav();
    public abstract String invCurrency();
    public abstract float sAndPRating();
    public abstract float moodysRating();
}

