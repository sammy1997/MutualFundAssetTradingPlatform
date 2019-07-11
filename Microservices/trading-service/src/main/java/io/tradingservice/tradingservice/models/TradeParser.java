package io.tradingservice.tradingservice.models;

public class TradeParser {

    private String fundNumber;
    private int quantity;
    private String status;


    public String getFundNumber() {
        return fundNumber;
    }

    public void setFundNumber(String fundNumber) {
        this.fundNumber = fundNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
