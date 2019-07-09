package com.mutualfundtrading.fundhandling.config;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {
    @Bean
    public EntitlementDAO createEntitlementDAO() {
        return new EntitlementDAO();
    }

    @Bean
    public FundDAO createFundDAO() {
        return new FundDAO();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
