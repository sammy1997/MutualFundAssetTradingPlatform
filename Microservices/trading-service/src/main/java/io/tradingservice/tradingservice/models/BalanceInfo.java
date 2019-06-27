package io.tradingservice.tradingservice.models;

public class BalanceInfo {
    float balance;
    String baseCurr;

    public BalanceInfo() {
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getBaseCurr() {
        return baseCurr;
    }

    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }
}
