package io.tradingservice.tradingservice.utils;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String SECRET_KEY = "ggmuekp69t11p6914qrl7pk4598679hm";
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

}
