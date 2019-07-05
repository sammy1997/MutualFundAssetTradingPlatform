package com.sapient.authservice;


import com.sapient.authservice.dao.UserDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class AuthServiceApplication  {


	@Bean
	public UserDAO createUsersDAO(){
		return new UserDAO();
	}


	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
