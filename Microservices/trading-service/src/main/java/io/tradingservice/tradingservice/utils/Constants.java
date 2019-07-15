package io.tradingservice.tradingservice.utils;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    // Secret key
    public static final String SECRET_KEY = "ggmuekp69t11p6914qrl7pk4598679hm";

    // FX Rates
    public static final Map<String, Float> FX_USD = new HashMap<String, Float>() {
                                                        {
                                                            put("INR", (float) 69.1605);
                                                            put("USD", (float) 1);
                                                            put("EUR", (float) 0.8799);
                                                            put("AED", (float) 3.6725);
                                                            put("GBP", (float) 0.7879);
                                                            put("SAR", (float) 3.7500);
                                                            put("JPY", (float) 107.6890);
                                                        }
                                                    };

    // Controller paths
    public static final String viewEndPoint = "/view";
    public static final String verifyEndPoint = "/verify";
    public static final String exchangeEndPoint = "/exchange";
    public static final String addUserEndPoint = "/addUser";
    public static final String addCurrencyEndpoint = "/add";
    public static final String getCurrencyEndpoint = "/get";
    public static final String getAllCurrencyEndpoint = "/all";
    public static final String updateCurrencyEndpoint = "/update";

    // Transaction Controller paths
    public static final String transactionHistoryEndPoint = "/history";
    public static final String addTransactionEndPoint = "/addTransaction";

    public static final String mongoPort = "mongodb://localhost:27017/UserTrades";


}
