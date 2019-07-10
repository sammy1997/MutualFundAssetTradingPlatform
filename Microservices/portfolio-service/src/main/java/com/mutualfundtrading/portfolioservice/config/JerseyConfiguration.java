package com.mutualfundtrading.portfolioservice.config;

import com.mutualfundtrading.portfolioservice.controller.PortfolioController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

@SuppressWarnings("ALL")
@Configuration
@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {
	public JerseyConfiguration() {}

	@PostConstruct
	public void setUp() {
		register(PortfolioController.class);
	}
}
