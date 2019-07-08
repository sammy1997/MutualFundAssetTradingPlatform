package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableUserParser.class)
@JsonDeserialize(as = ImmutableUserParser.class)
public interface UserParser
{
    Optional<String> userId();
//    float balance();
    float currBal();
    Optional<String> baseCurr();
    Optional<List<Fund>> all_funds();
}
