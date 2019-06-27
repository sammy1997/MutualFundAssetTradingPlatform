package io.tradingservice.tradingservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableUser2.class)
@JsonDeserialize(as = ImmutableUser2.class)
public interface User2 {
    String userId();
    float balance();
    List<FundParser> all_funds();



}
