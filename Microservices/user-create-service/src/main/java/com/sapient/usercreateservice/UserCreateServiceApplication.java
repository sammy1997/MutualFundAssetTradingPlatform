package com.sapient.usercreateservice;

import com.sapient.usercreateservice.dao.UsersDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@EnableEurekaClient
public class UserCreateServiceApplication {

	@Bean
	public UsersDAO createUsersDAO(){
		return new UsersDAO();
	}
	public static void main(String[] args) {
		SpringApplication.run(UserCreateServiceApplication.class, args);
	}

}
