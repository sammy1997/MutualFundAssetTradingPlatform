package io.tradingservice.tradingservice.configurations;

import io.tradingservice.tradingservice.controllers.FXRateController;
import io.tradingservice.tradingservice.controllers.TradeServiceController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TradeServiceController.class);
        register(FXRateController.class);
    }
}
