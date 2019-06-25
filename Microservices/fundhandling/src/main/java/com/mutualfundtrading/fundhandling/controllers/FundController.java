package com.mutualfundtrading.fundhandling.controllers;


import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.FundService;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import com.sun.jersey.multipart.FormDataParam;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Path("/funds")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FundController{
    @Autowired
    FundService service;

//    Fund creation url
    @Path("/create")
    @POST
    public String createFund(Fund fund){
        System.out.println(fund.fundNumber());
        return service.addFundService(fund);
    }

//    Fund details update URL
    @Path("/retrieve")
    @POST
    public ImmutableFundDBModel getFund(Fund fund){
        return service.getFund(fund.fundNumber());
    }

//    Get all funds
    @Path("/all")
    @GET
    public List<ImmutableFundDBModel> getAll(@HeaderParam("Authorization") String header){
        ServiceUtils.decodeJWTForUserId(header);
        return service.getAll();
    }

//    Update fund details
    @Path("/update")
    @PATCH
    public Message updateFund(Fund fund){
        return service.update(fund);
    }

//    Delete funds
    @Path("/delete")
    @DELETE
    public Message delete(@QueryParam("fundNumber") String fundNumber){
        return service.delete(fundNumber);
    }

//    Search funds
    @Path("/search")
    @GET
    public List<FundDBModel> search(@QueryParam("field") String field, @QueryParam("term") String searchTerm){
        return service.searchAllFunds(field, searchTerm);
    }

//    Add from CSV. TODO

    @Path("/addFromFile")
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public Message addFromFile(@FormDataParam("file") InputStream fileInputStream){
        System.out.println(fileInputStream.toString());
//        System.out.println(file);
        return null;
    }
}
