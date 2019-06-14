package com.mutualfundtrading.fundhandling.controllers;


import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.FundService;
import com.sun.jersey.multipart.FormDataParam;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@Path("/funds")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FundController{
    @Autowired
    FundService service;
    @Path("/create")
    @POST
    public String createFund(Fund fund){
        return service.addFundService(fund);
    }

    @Path("/retrieve")
    @POST
    public ImmutableFundDBModel getFund(Fund fund){
        return service.getFund(fund.fundNumber());
    }

    @Path("/all")
    @GET
    public List<ImmutableFundDBModel> getAll(){
        return service.getAll();
    }

    @Path("/update")
    @PATCH
    public Message updateFund(Fund fund){
        return service.update(fund);
    }

    @Path("/delete")
    @DELETE
    public Message delete(Fund fund){
        return service.delete(fund);
    }

    @Path("/addFromFile")
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public Message addFromFile(@FormDataParam("file") InputStream fileInputStream){
        System.out.println(fileInputStream.toString());
//        System.out.println(file);
        return null;
    }
}
