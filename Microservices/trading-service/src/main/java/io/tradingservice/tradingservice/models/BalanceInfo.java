package io.tradingservice.tradingservice.models;

public class BalanceInfo {

    private float currBal;
    private String baseCurr;


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
