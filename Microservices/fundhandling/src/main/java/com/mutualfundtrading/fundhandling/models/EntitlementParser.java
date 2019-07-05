package com.mutualfundtrading.fundhandling.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableEntitlementParser.class)
@JsonDeserialize(as = ImmutableEntitlementParser.class)
public interface EntitlementParser {
    Optional<String> userId();
    Optional<List<String>> entitledTo();
}