package io.tradingservice.tradingservice.utils;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    // Secret key
    public static final String SECRET_KEY = "ggmuekp69t11p6914qrl7pk4598679hm";

    // Controller paths
    public static final String viewEndPoint = "/view";
    public static final String verifyEndPoint = "/verify";
    public static final String exchangeEndPoint = "/exchange";
    public static final String addUserEndPoint = "/addUser";
    public static final String addCurrencyEndpoint = "/add";
    public static final String getCurrencyEndpoint = "/get";
    public static final String getAllCurrencyEndpoint = "/all";
    public static final String updateCurrencyEndpoint = "/update";

    public static final String mongoPort = "mongodb://localhost:27017/UserTrades";


}
