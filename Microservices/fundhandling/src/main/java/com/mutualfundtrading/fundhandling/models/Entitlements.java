package com.mutualfundtrading.fundhandling.models;

import org.immutables.mongo.Mongo;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Mongo.Repository(collection = "Entitlements")
public interface Entitlements {
    @Mongo.Id
    String userId();
    List<String> entitledTo();
}
