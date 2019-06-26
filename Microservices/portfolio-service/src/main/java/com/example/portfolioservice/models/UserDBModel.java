package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;


@Value.Immutable
@Mongo.Repository(collection = "User")
@JsonSerialize(as = ImmutableUserDBModel.class)
@JsonDeserialize(as = ImmutableUserDBModel.class)
public interface UserDBModel
{
    @Mongo.Id
    String userId();
    float balance();
    String baseCurr();
    List<Fund2> all_funds();
}
