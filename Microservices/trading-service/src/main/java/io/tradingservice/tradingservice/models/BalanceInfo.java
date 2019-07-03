package io.tradingservice.tradingservice.models;

public class BalanceInfo {
    float currBal;
    String baseCurr;

    public BalanceInfo() {
    }

    public float getCurrBal() {
        return currBal;
    }

    public void setCurrBal(float currBal) {
        this.currBal = currBal;
    }

    public String getBaseCurr() {
        return baseCurr;
    }

    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }
}
