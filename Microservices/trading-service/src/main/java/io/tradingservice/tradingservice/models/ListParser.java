package io.tradingservice.tradingservice.models;

import java.util.List;

public class ListParser {

    private List<ImmutableFund> entitlements;

    public List<ImmutableFund> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<ImmutableFund> entitlements) {
        this.entitlements = entitlements;
    }
}
