package com.mutualfundtrading.fundhandling;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
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
public class FundhandlingApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FundhandlingApplication.class);
	}

//	@Bean
//	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
//		return new HiddenHttpMethodFilter() {
//			@Override
//			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//											FilterChain filterChain) throws ServletException, IOException {
//				if ("POST".equals(request.getMethod())
//						&& request.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
//					filterChain.doFilter(request, response);
//				} else {
//					super.doFilterInternal(request, response, filterChain);
//				}
//			}
//		};
//	}

	@Bean
	public EntitlementDAO createEntitlementDAO(){
		return new EntitlementDAO();
	}

	public static void main(String[] args) {
		final Application application = new ResourceConfig()
				.packages("org.glassfish.jersey.examples.multipart")
				.register(MultiPartFeature.class);
		SpringApplication.run(FundhandlingApplication.class, args);
	}

}
