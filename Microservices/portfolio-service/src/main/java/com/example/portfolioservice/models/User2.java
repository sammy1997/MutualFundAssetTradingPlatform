package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableUser2.class)
@JsonDeserialize(as = ImmutableUser2.class)
public interface User2
{
    String userId();
    float balance();
    Optional<List<Fund2>> all_funds();
}
