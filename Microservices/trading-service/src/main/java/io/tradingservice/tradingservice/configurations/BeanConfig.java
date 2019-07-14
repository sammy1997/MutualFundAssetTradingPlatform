package io.tradingservice.tradingservice.configurations;

import io.tradingservice.tradingservice.repositories.FXRateAccessObject;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public UserAccessObject getUserAccessObject(){
        return new UserAccessObject();
    }

    @Bean
    public FXRateAccessObject getFXRateAccessObject(){
        return new FXRateAccessObject();
    }
}
