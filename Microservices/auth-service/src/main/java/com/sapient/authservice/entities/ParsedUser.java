package com.sapient.authservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.sapient.authservice.entities.ImmutableUser;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableParsedUser.class)
@JsonDeserialize(as = ImmutableParsedUser.class)
public interface ParsedUser {
    String userId();
    String password();
    Optional<String> fullName();
    Optional<Double> currBal();
    Optional<String> baseCurr();
    Optional<String> role();
}
