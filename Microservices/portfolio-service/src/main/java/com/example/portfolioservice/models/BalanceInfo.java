package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(as = BalanceInfo.class)
@JsonSerialize(as = BalanceInfo.class)
public class BalanceInfo {
    String baseCurr;
    float balance;

    public BalanceInfo() {
    }

    public String getBaseCurr() {
        return baseCurr;
    }

    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
