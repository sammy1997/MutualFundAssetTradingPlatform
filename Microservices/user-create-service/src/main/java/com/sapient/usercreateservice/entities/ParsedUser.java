package com.sapient.usercreateservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableParsedUser.class)
@JsonDeserialize(as = ImmutableParsedUser.class)
public interface ParsedUser {
    String userId();
    String password();
    String baseCurr();
    Optional<String> fullName();
    Optional<Float> currBal();
    Optional<String> role();
}
