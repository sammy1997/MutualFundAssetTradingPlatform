package com.sapient.usercreateservice.controller;

import com.sapient.usercreateservice.entities.ImmutableUser;
import com.sapient.usercreateservice.entities.ParsedUser;
import com.sapient.usercreateservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.List;



@Path("/")
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    List<String> header;
    String authHeader;
//
    @Path("/list-all")
    @GET
    @Produces("application/json")
    public List<ImmutableUser> getAllUsers(){
        LOGGER.info("List-all users end point accessed");
        return userService.getAllUsers();
    }

    @Path("/update")
    @POST
    @Consumes("application/json")
    public Response updateUser(@RequestBody ParsedUser user){
        LOGGER.info("Update a user end point accessed");
        return userService.updateUser(user);
    }

    @POST
    @Consumes("application/json")
    public String addUser(@RequestBody ParsedUser user) {
        Response response = userService.addUser(user);
        if (response.getStatus() == 200 && !user.role().get().equals("ROLE_ADMIN")) {
            String userCredentials = "{\"userId\": \"" + user.userId() + "\", \n" +
                    "\"password\": \"" + user.password() + "\"\n" +
                    "}";

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
            LOGGER.info("Create User in UserList DB end point accessed");

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
            LOGGER.info("Create User in portfolio end point accessed");

            webClientBuilder.build()
                    .post()
                    .uri("localhost:8762/trade/addUser?secret=ggmuekp69t11p6914qrl7pk4598679hm")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .exchange()
                    .doOnSuccess(clientResponse -> {
                        System.out.println(clientResponse.statusCode());
                    }).block();
            LOGGER.info("Create User in UserTrades end point accessed");
            return user.userId() + " added to database";
        } else if (response.getStatus() == 400) {
            return "Some field(s) missing. If not, please validate your fields";
        }else if(response.getStatus() == 406){
            return "User already exists in the database. Cannot add another instance.";
        }else if (user.role().get().equals("ROLE_ADMIN")) {
            return "Admin role created";
        }else {
            return "Some unknown error occurred";
        }
    }
}
