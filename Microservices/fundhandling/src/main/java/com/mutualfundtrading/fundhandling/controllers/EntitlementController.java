package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Entitlements;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableEntitlements;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.EntitlementService;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import java.util.List;


@Path("/entitlements")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class EntitlementController {

    @Autowired
    EntitlementService service;

    @Path("/add")
    @POST
//    @QueryParam("userId") String userId, @QueryParam("entitledTo") List<String> entitleTo
    public Message createEntitlement(Entitlements entitlement){
        return service.addEntitlements(entitlement);
    }

    @Path("/delete")
    @DELETE
    public Message deleteEntitlements(Entitlements entitlement){
        return service.deleteEntitlements(entitlement);
    }

    @Path("/get")
    @GET
    public List<ImmutableFundDBModel> getEntitlements(@QueryParam("userId") String userId){
        return service.getEntitlements(userId);
    }

    @Path("/search")
    @GET
    public List<FundDBModel> searchEntitlements(@QueryParam("userId") String userId, @QueryParam("field") String field,
                                                @QueryParam("term") String searchTerm){
        return service.searchEntitlements(userId, field, searchTerm);
    }

}
