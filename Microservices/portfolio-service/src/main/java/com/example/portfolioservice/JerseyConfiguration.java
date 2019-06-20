package com.example.portfolioservice;

import com.example.portfolioservice.controller.PortfolioController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {
	public JerseyConfiguration() {
		
	}
	
	@PostConstruct
	public void setUp() {
		register(PortfolioController.class);
	//	register(GenericExceptionMapper.class);
	}
}
