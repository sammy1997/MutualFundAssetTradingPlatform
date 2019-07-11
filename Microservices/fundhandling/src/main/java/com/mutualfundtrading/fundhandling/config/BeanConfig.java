package com.mutualfundtrading.fundhandling.config;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
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

    @Bean
    public ServiceUtils serviceUtils(){
        return new ServiceUtils();
    }
}
