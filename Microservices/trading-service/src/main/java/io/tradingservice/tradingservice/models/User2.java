package io.tradingservice.tradingservice.models;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface User2 {
    String userId();
    float balance();
    List<ImmutableTrade> trades();

}
