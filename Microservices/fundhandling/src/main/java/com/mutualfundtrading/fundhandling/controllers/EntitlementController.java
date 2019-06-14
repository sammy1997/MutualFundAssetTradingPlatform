package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.ImmutableEntitlements;
import com.mutualfundtrading.fundhandling.services.EntitlementService;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import java.util.List;

//@RestController
@Path("/entitlements")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class EntitlementController {

    @Autowired
    EntitlementService service;

    @Path("/add")
    @POST
    public Message createEntitlement(@QueryParam("userId") String userId, @QueryParam("entitledTo") List<String> entitleTo){
//        return null;
        return service.addEntitlements(userId, entitleTo);
    }
}
