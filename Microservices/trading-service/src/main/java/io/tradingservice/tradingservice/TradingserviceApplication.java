package io.tradingservice.tradingservice;

import io.tradingservice.tradingservice.repositories.UserAccessObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class TradingserviceApplication {

//    @Bean
//    UserAccessObject userAccessObject(){
//        return new UserAccessObject();
//    }

    public static void main(String[] args) {
        SpringApplication.run(TradingserviceApplication.class, args);
    }

}
