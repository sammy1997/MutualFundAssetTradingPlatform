package io.tradingservice.tradingservice.models;

import java.util.List;

public class ListParser {
    List<ImmutableFund> entitlements;

    public ListParser() {
    }

    public List<ImmutableFund> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<ImmutableFund> entitlements) {
        this.entitlements = entitlements;
    }
}
