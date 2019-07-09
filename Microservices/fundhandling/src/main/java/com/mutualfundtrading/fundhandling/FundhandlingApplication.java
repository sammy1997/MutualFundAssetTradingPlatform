package com.mutualfundtrading.fundhandling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication()
@EnableEurekaClient
public class FundhandlingApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FundhandlingApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FundhandlingApplication.class, args);
    }

}
