package com.example.portfolioservice.models;

public class Fund
{
    String fundNumber, fundName;
    double presentNAV, originalNAV;

    String invManager;

    double sAndPRating, moodysRating, profit;

    public Fund()
    {

    }

    public Fund(String fundNumber, String fundName, double profit)
    {
        this.fundNumber = fundNumber;
        this.fundName = fundName;
        this.profit = profit;
      //  this.presentNAV = presentNAV;
        //this.originalNAV = originalNAV;
    }

    public Fund(String fundNumber, String fundName, String InvManager, double originalNAV, double SnPrating, double moodysRating)
    {
        this.fundName = fundName;
        this.fundNumber = fundNumber;
        this.invManager = InvManager;
        this.originalNAV = originalNAV;
        this.sAndPRating = SnPrating;
        this.moodysRating = moodysRating;
    }
    public Fund(String fundNumber, String fundName, String InvManager, double originalNAV,
                double presentNAV, double SnPrating, double moodysRating)
    {
        this.fundName = fundName;
        this.fundNumber = fundNumber;
        this.invManager = InvManager;
        this.originalNAV = originalNAV;
        this.sAndPRating = SnPrating;
        this.moodysRating = moodysRating;
        this.presentNAV = presentNAV;
        this.profit = presentNAV - originalNAV;
    }
    public double getPresentNAV() {
        return presentNAV;
    }

    public double getOriginalNAV() {
        return originalNAV;
    }

    public String getInvManager() {
        return invManager;
    }

    public double getSnPrating() {
        return sAndPRating;
    }

    public double getMoodysRating() {
        return moodysRating;
    }
// int

    public String getFundNumber() {
        return fundNumber;
    }

    public void setFundNumber(String fundNumber) {
        this.fundNumber = fundNumber;
    }

    public String getFundName() {
        return fundName;
    }

    public double getProfit()
    {
        return profit;
    }

    public void setProfit(double profit)
    {
        this.profit = profit;
    }

    public void setFundName(String fundName)
    {
        this.fundName = fundName;
    }
//
//    public double getPresentNAV() {
//        return presentNAV;
//    }
//
    public void setPresentNAV(float presentNAV)
    {
        this.presentNAV = presentNAV;
    }
//
//    public double getOriginalNAV() {
//        return originalNAV;
//    }
//
    public void setOriginalNAV(float originalNAV)
    {
        this.originalNAV = originalNAV;
    }
}
