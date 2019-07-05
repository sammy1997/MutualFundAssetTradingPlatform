package com.example.portfolioservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(as = BalanceInfo.class)
@JsonSerialize(as = BalanceInfo.class)
public class BalanceInfo {
    String baseCurr;

    //    float balance;
    float currBal;


    public BalanceInfo() {
    }

    public String getBaseCurr() {
        return baseCurr;
    }

    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }

    public float getCurrBal() {
        return currBal;
    }

    public void setCurrBal(float currBal) {
        this.currBal = currBal;
    }



}
