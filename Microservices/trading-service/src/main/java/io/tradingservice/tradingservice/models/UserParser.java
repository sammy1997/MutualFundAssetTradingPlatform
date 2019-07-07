package io.tradingservice.tradingservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableUserParser.class)
@JsonDeserialize(as = ImmutableUserParser.class)
public interface UserParser {

    String userId();
    float currBal();
    List<FundParser> all_funds();



}
