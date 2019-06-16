package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Mongo.Repository(collection = "Entitlements")
@JsonSerialize(as = ImmutableEntitlementsDBModel.class)
@JsonDeserialize(as = ImmutableEntitlementsDBModel.class)
public interface EntitlementsDBModel {
    @Mongo.Id
    String userId();
    List<String> entitledTo();
}
