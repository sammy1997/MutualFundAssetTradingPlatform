package com.sapient.usercreateservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableUsers.class)
@JsonDeserialize(as = ImmutableUsers.class)
public interface Users {
    String userId();
    Optional<String> password();
    Optional<String> fullName();
    Optional<Double> currBal();
    Optional<String> baseCurr();
    Optional<String> role();
}
