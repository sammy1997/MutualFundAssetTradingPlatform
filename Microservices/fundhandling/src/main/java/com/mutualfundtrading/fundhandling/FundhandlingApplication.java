package com.mutualfundtrading.fundhandling;

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

	public static void main(String[] args) {
		SpringApplication.run(FundhandlingApplication.class, args);
	}

}
