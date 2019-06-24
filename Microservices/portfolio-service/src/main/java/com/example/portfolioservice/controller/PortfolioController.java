package com.example.portfolioservice.controller;


import com.example.portfolioservice.models.Fund;
import com.example.portfolioservice.models.User;
import com.example.portfolioservice.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;
//
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.example.portfolioservice.models.links;
@Component
@Path("/")
public class PortfolioController
{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PortfolioService portfolioService;
    @GET
    @Produces("application/json")
    @Path("/")
    public Collection<User> getf()
    {
        return portfolioService.getUser();
    }
    @GET
    @Produces("application/json")
    @Path("/{userId}")
    public Optional<User> getUserById(@PathParam("userId") String userId)
    {
        return portfolioService.getUserById(userId);
    }
//    @DELETE
//    @Produces("application/json")
//    @Path("/delete/{userId}")
//    public Optional<User> deleteUserById(@PathParam("userId") String userId)
//    {
//        return portfolioService.deleteUserById(userId);
//    }
    @GET
    @Produces("application/json")
    @Path("/update/{userId}")
    public User updateUserById(@PathParam("userId") String userId)
    {

        User user = new User(userId);
        portfolioService.deleteUserById(userId);
        //System.out.println(userUpdatePayload.getAll_funds());
        //Fund Fund = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, Fund.class);
//        List<Fund> funds = Arrays.asList(
//                new Fund("1", "number1","onemoremanager", 3.45, 1.35, 2.01),
//                new Fund("2", "number2","onemoremanager", 4.56, 2.33, 1.98),
//                new Fund("3", "number3","onemoremanager", 4.56, 2.33, 1.98)
//
//        );
//        links l1 = new links();
//        ArrayList<Fund> new_funds = new ArrayList<>();
//
//        funds.stream().map(Fund -> {
//
//            double f1 = restTemplate.getForObject(l1.getLink1(), Double.class);
//
//            // Fund fun2 = restTemplate.getForObject("http://localhost:8082/movies/1", Fund.class);
//            //double f2 = restTemplate.getForObject(l1.getLink2(), Double.class);
//
//            Fund ff =  new Fund(Fund.getFundNumber(),Fund.getFundName(),Fund.getInvManager(), Fund.getOriginalNAV(), f1,
//                    Fund.getSnPrating(), Fund.getMoodysRating());
//            new_funds.add(ff);
//            return ff;
////
//        }).collect(Collectors.toList());
//        User.setAll_funds(new_funds);
        return insert(user);
        //return portfolioService.createUser(User);

//        User.setAll_funds(userUpdatePayload.getAll_funds());
//        portfolioService.deleteUserById(userId);
//        return portfolioService.createUser(User);
        //return portfolioService.updateUserById(userId, userUpdatePayload);
    }
    private User insert(User user)
    {
        //Fund Fund = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, Fund.class);
        List<Fund> funds = Arrays.asList(
                new Fund("1", "number1","onemoremanager", 3.45, 1.35, 2.01),
                new Fund("2", "number2","onemoremanager", 4.56, 2.33, 1.98),
                new Fund("3", "number3","onemoremanager", 4.56, 2.33, 1.98)

        );
        links l1 = new links();
        ArrayList<Fund> new_funds = new ArrayList<>();

        funds.stream().map(fund -> {

//            double f1 = restTemplate.getForObject(l1.getLink1(), Double.class);
            double f1 = 20.3;

            // Fund fun2 = restTemplate.getForObject("http://localhost:8082/movies/1", Fund.class);
            //double f2 = restTemplate.getForObject(l1.getLink2(), Double.class);

            Fund ff =  new Fund(fund.getFundNumber(),fund.getFundName(),fund.getInvManager(), fund.getOriginalNAV(), f1,
                    fund.getSnPrating(), fund.getMoodysRating());
            new_funds.add(ff);
            return ff;
//
        }).collect(Collectors.toList());
        user.setAll_funds(new_funds);
        return portfolioService.createUser(user);
    }
    @GET
    @Produces("application/json")
    @Path("/{userId}/abc")
    //
    public List<Fund> getFunds(@PathParam("userId") String userId)
    {
        Optional<User> u = portfolioService.getUserById(userId);
        if(u.isPresent())
        {
            return u.get().getAll_funds();

        }
        //Fund Fund = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, Fund.class);
        List<Fund> funds = Arrays.asList(
                new Fund("1", "number1","manager", 3.45, 1.35, 2.01),
                new Fund("2", "number2","anothermanager", 4.56, 2.33, 1.98),
                new Fund("3", "number3","onemoremanager", 4.56, 2.33, 1.98)

        );
        links l1 = new links();
        User user = new User();
        user.setUserid(userId);
        user = insert(user);
        return user.getAll_funds();
//        ArrayList<Fund> l = new ArrayList<>();
//
//        funds.stream().map(Fund -> {
//
//            double f1 = restTemplate.getForObject(l1.getLink1(), Double.class);
//
//           // Fund fun2 = restTemplate.getForObject("http://localhost:8082/movies/1", Fund.class);
//            //double f2 = restTemplate.getForObject(l1.getLink2(), Double.class);
//
//            Fund ff =  new Fund(Fund.getFundNumber(),Fund.getFundName(),Fund.getInvManager(), Fund.getOriginalNAV(), f1,
//                    Fund.getSnPrating(), Fund.getMoodysRating());
//            l.add(ff);
//            return ff;
////
//        }).collect(Collectors.toList());
//        User.setAll_funds(l);
//        portfolioService.createUser(User);
//        return l;

    }

}
