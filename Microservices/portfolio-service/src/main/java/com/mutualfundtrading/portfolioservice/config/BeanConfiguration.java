package com.mutualfundtrading.portfolioservice.config;

import com.mutualfundtrading.portfolioservice.DAO.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {
    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().build();
    }

    @Bean
    public UserDAO newUserDAO() {
        return new UserDAO();
    }
}
