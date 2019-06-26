package com.example.portfolioservice;

import com.example.portfolioservice.DAO.UserDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
public class PortfolioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioServiceApplication.class, args);
	}

	//@LoadBalanced
	@Bean
	public WebClient getWebClient() {
		return WebClient.builder().build();
	}

	@Bean
	public UserDAO newUserDAO(){
		return new UserDAO();
	}
}
