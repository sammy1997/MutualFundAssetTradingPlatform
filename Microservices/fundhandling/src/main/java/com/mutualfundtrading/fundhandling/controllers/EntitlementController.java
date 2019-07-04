package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.Entitlements;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableEntitlements;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.EntitlementService;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Path("/entitlements")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class EntitlementController {

    @Autowired
    EntitlementService service;

    @Path("/add")
    @POST
    public Response createEntitlement(Entitlements entitlement){
        Optional<String> userId = entitlement.userId();
        if (!userId.isPresent()){
            return Response.status(400).entity("Invalid request").build();
        }
        return service.addEntitlements(entitlement);
    }

    @Path("/delete")
    @DELETE
    public Response deleteEntitlements(Entitlements entitlement, @HeaderParam("Authorization") String token){
        String userId = ServiceUtils.decodeJWTForUserId(token);
        if (userId==null){
            return Response.status(401).entity("Invalid authorization token").build();
        }
        entitlement = ImmutableEntitlements.builder().from(entitlement).build();
        return service.deleteEntitlements(entitlement);
    }

    @Path("/get")
    @GET
    public List<ImmutableFundDBModel> getEntitlements(@HeaderParam("Authorization") String token){
        return service.getEntitlements(ServiceUtils.decodeJWTForUserId(token));
    }

    @Path("/search")
    @GET
    public List<FundDBModel> searchEntitlements(@HeaderParam("Authorization") String token, @QueryParam("field") String field,
                                                @QueryParam("term") String searchTerm){
        return service.searchEntitlements(ServiceUtils.decodeJWTForUserId(token), field, searchTerm);
    }

    @Path("/get/fund")
    @GET
    public FundDBModel getEntitledFundDetail(@HeaderParam("Authorization") String token, @QueryParam("fundNumber") String fundId){
        List<FundDBModel> result = service.searchEntitlements(ServiceUtils.decodeJWTForUserId(token), "Fund Number", fundId);
        if (result!=null){
            if (result.size()>0){
                return result.get(0);
            }
            return null;
        }
        return null;
    }

    @POST
    @Path("/addEntitlements")
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response uploadCsvFile(  @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData)
    {
        int status = ServiceUtils.fileUpload(fileInputStream, fileMetaData);
        if (status ==404){
            return Response.status(status).entity("Provide excel or csv files").build();
        }else if (status == 400){
            return Response.status(status).entity("Error while uploading file. Try again").build();
        }else {
            return ServiceUtils.addEntitlementsFromCSV(fileMetaData.getFileName());
        }
    }
}
