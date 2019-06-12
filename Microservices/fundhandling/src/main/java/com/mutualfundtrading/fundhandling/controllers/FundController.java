package com.mutualfundtrading.fundhandling.controllers;


import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import java.util.List;

@RestController
@Path("/funds")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FundController{
    @Autowired
    FundService service;

    @Path("/create")
    @POST
    public String createFund(@QueryParam("fundName") String fundName, @QueryParam("fundNumber") String fundNumber,
                             @QueryParam("invManager") String invManager, @QueryParam("setCycle") int setCycle,
                             @QueryParam("nav") float nav, @QueryParam("invCurrency") String invCurrency,
                             @QueryParam("sAndPRating") float sAndPRating, @QueryParam("moodysRating") float moodysRating){
//        System.out.println("Hello " + fundName + ", " + fundNumber);
//        return null;
        return service.addFundService(fundName, fundNumber, invManager, setCycle, nav, invCurrency, sAndPRating, moodysRating);
    }

    @Path("/retrieve")
    @POST
    public ImmutableFund getFund(@QueryParam("fundNumber") String fundNumber){
        System.out.println("Hello " + fundNumber);
        return service.getFund(fundNumber);
    }

//    @Path("/retrieve")
//    @POST
//    public ImmutableFund getFund(@RequestBody ImmutableFund fund){
//        System.out.println(fund.fundName());
//        return null;
////        return service.getFund(fundNumber);
//    }

    @Path("/all")
    @GET
    public List<ImmutableFund> getAll(){
        return service.getAll();
    }
}
