package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("CheckStyle")
@JsonDeserialize(as = BalanceInfo.class)
@JsonSerialize(as = BalanceInfo.class)
public class BalanceInfo {

    private String baseCurr;

    private float currBal;

    public BalanceInfo() {
    }

    public String getBaseCurr() {
        return baseCurr;
    }

    public void setBaseCurr(final String baseCurr) {
        this.baseCurr = baseCurr;
    }

    public float getCurrBal() {
        return currBal;
    }

    public void setCurrBal(final float currBal) {
        this.currBal = currBal;
    }



}
