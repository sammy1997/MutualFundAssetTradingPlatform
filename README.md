# Mutual Fund Asset Trading Platform

## Requirements:

### Use Case

Develop trading platform for Mutual Fund Asset class. 

### Fund Finder Page

There should be a fund finder UI where user can search for funds. User can go to trading page from fund finder screen. When user selects the fund(s) from fund finder screen, trade-blotter screen should be filled automatically with fund details. User should only input the trading type (redemption/purchase) and the quantity. User can trade only on the entitled funds. No non-entitled funds should appear on fund finder screen for that user. Each fund must show following fields.

*	Fund Name
*	Investment Manager
*	Fund Number
*	Settlement Cycle
*	NAV
*	Investment Currency
*	S&P Rating
*	Moody’s Rating

### Portfolio Page

Portfolio UI should show all the trades that has been done by user. User can go to trading page from portfolio screen. When user selects the fund(s) from portfolio screen, tradeblotter screen should be filled automatically with fund details. User should only input the trading type (redemption/purchase) and the quantity. User can trade only on the entitled funds. No non-entitled funds should appear on fund finder screen. For each traded fund, user should be able to see the following fields.

*	Fund Name
*	Investment Manager
*	Fund Number
*	Settlement Cycle
*	Investment Currency
*	S&P Rating
*	Moody’s Rating
*	Purchase NAV
*	Current NAV
*	Profit/Loss Difference Amount
*	Profit/Loss Difference Percentage

### Trading Page

In trading, user can either purchase or redeem the mutual funds. User can place trade simultaneously maximum 5 trades. These 5 trades can be for different mutual funds. Each mutual fund will be associated with investment currency. User should not be able to place trade if their account balance is less than the trade balance. While redeeming the mutual funds, user must have at-least traded quantity in their portfolio against that mutual fund. While trading, FXRates needs to be applied from base currency to investment currency, if there is a change of investment currency from base currency. User can trade only on the entitled funds. User should not be allowed on non-entitled funds.

### Support Page

Support team should be able to add funds to the database. They should be able to entitle funds to user(s).

### Other Requirements

UI must give out the flexibility to store user preferences for each user. As of now, only base currency is part of user preferences. However in future there may appear other user preferences as well. 
In case of no activity for 15 minutes, user should be logged out from UI. As part of security, a proper rate limiting system should be implemented. Any unauthorized user should not get access to any of the service. Even if user is authorized, if user doesn’t have proper access, shouldn’t be allowed to access those services. Support team should get only support screen. Trading user should not see support screen.
