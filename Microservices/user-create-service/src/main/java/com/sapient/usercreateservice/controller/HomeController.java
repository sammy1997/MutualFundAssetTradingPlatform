package com.sapient.usercreateservice.controller;

import com.sapient.usercreateservice.entities.Users;
import com.sapient.usercreateservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.util.List;



@Path("/")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    List<String> header;
    String authHeader;

    @POST
    @Consumes("application/json")
    public String addUser(@RequestBody Users user) {
        Response response = userService.addUser(user);
        if (response.getStatus() == 200 && !user.role().get().equals("ROLE_ADMIN")) {
            String userCredentials = "{\"userId\": \"" + user.userId() + "\", \n" +
                    "\"password\": \"" + user.password() + "\"\n" +
                    "}";

            String currBal = "{\"currBal\": \"" + user.currBal() + "\"\n" + "}";


            //Make a post request to auth-service to generate a token that will be used to make a request to portfolio service
            webClientBuilder.build()
                    .post()
                    .uri("localhost:8762/auth")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(userCredentials))
                    .exchange()
                    .doOnSuccess(clientResponse -> {
                        if (clientResponse.statusCode().value() == 200) {
                            header = (clientResponse.headers().header("Authorization"));
                        }
                    })
                    .block();

            for (String temp : header) {
                authHeader = temp;
            }

            // Use the token from above to update balance in portfolio service
            webClientBuilder.build()
                    .post()
                    .uri("localhost:8762/portfolio/add/user?secret=ggmuekp69t11p6914qrl7pk4598679hm&balance="+
                            user.currBal().get()+ "&baseCurr="+user.baseCurr())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .exchange()
                    .doOnSuccess(clientResponse -> {
                        System.out.println(clientResponse.statusCode());
                    }).block();
            return user.userId() + " added to database";
        } else if (response.getStatus() == 400) {
            return "Some field(s) missing. If not, please validate your fields";
        }else if (user.role().get().equals("ROLE_ADMIN")) {
            return "Admin role created";
        }else{
            return user.userId() + " already exists in the database. Cannot add another instance.";
        }
    }
}
