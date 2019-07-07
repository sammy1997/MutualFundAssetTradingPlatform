package io.tradingservice.tradingservice.models;

public class FundParser {

    String fundNumber;
    float originalNav;
    int quantity;

    public FundParser() {
    }

    public String getFundNumber() {
        return fundNumber;
    }

    public void setFundNumber(String fundNumber) {
        this.fundNumber = fundNumber;
    }

    public float getOriginalNav() {
        return originalNav;
    }

    public void setOriginalNav(float originalNav) {
        this.originalNav = originalNav;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
