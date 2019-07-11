package io.tradingservice.tradingservice.models;

public class FundParser {

    private String fundNumber;
    private float originalNav;
    private int quantity;

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
