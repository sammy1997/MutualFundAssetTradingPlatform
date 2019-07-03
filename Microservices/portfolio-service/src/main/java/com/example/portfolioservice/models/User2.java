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
    Optional<String> userId();
//    float balance();
    float currBal();
    Optional<String> baseCurr();
    Optional<List<Fund2>> all_funds();
}
