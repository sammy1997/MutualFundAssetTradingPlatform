package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.HashSet;
import java.util.List;


@Value.Immutable
@JsonSerialize(as = ImmutableEntitlements.class)
@JsonDeserialize(as = ImmutableEntitlements.class)
public interface Entitlements {
    String userId();
    List<String> entitledTo();
}
