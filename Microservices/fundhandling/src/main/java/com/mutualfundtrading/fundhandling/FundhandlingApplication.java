package com.mutualfundtrading.fundhandling;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.FundDBModelRepository;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import java.io.IOException;

@SpringBootApplication
@EnableEurekaClient
public class FundhandlingApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FundhandlingApplication.class);
	}


	@Bean
	public EntitlementDAO createEntitlementDAO(){
		return new EntitlementDAO();
	}

	@Bean
	public FundDAO createFundDAO(){
		return new FundDAO();
	}
//
//	@Bean
//	public FundDBModelRepository fundDBModelRepository(){
//		return
//	}

	public static void main(String[] args) {
		SpringApplication.run(FundhandlingApplication.class, args);
	}

}
