package com.sapient.usercreateservice.controller;

import com.sapient.usercreateservice.entities.Users;
import com.sapient.usercreateservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

//@RestController
//@RequestMapping("/")
@Path("/")
public class HomeController {

    @Autowired
    private UserService userService;

//    @PostMapping("/")
    @POST
    @Consumes("application/json")
    public String addUser(@RequestBody Users user){
//        System.out.println(user.username() + " added");
        userService.addUser(user);
        return user.userId() + " added to DB";
    }



}
