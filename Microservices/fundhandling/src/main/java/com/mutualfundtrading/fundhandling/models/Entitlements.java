package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Mongo.Repository(collection = "Entitlements")
@JsonSerialize(as = ImmutableEntitlements.class)
@JsonDeserialize(as = ImmutableEntitlements.class)
public interface Entitlements {
    @Mongo.Id
    String userId();
    List<String> entitledTo();
}
