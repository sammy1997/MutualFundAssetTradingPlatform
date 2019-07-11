package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.EntitlementParser;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.services.EntitlementServiceModel;
import com.mutualfundtrading.fundhandling.services.FundServiceModel;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;


@Path("/entitlements")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class EntitlementController implements EntitlementControllerModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(FundController.class);

    @Autowired
    private EntitlementServiceModel service;

    @Autowired
    private FundServiceModel fundService;

    @Autowired
    private ServiceUtils serviceUtils;

    @Path("/add")
    @POST
    public Response createEntitlement(@HeaderParam("Authorization") String token, EntitlementParser entitlement) {
        LOGGER.info("Entitlement add endpoint hit.");
        System.out.println(service.addEntitlements(entitlement, token).getStatus());
        return service.addEntitlements(entitlement, token);
    }

    @Path("/update")
    @POST
    public Response updateEntitlements(EntitlementParser entitlement) {
        LOGGER.info("Entitlement update endpoint hit.");
        return service.updateEntitlements(entitlement);
    }

    @Path("/delete")
    @DELETE
    public Response deleteEntitlements(EntitlementParser entitlement, @HeaderParam("Authorization") String token) {
        LOGGER.info("Delete entitlements endpoint hit");
        return service.deleteEntitlements(entitlement);
    }

    @Path("/get")
    @GET
    public List<Fund> getEntitlements(@HeaderParam("Authorization") String token) {
        LOGGER.info("Get entitlements endpoint hit.");
        return service.getEntitlements(ServiceUtils.decodeJWTForUserId(token));
    }

    @Path("/search")
    @GET
    public List<Fund> searchEntitlements(@HeaderParam("Authorization") String token,
                                         @QueryParam("field") String field,
                                         @QueryParam("term") String searchTerm) {
        LOGGER.info("Search entitlements endpoint hit.");
        return service.searchEntitlements(ServiceUtils.decodeJWTForUserId(token), field, searchTerm);
    }

    @Path("/get/fund")
    @GET
    public Fund getEntitledFundDetail(@HeaderParam("Authorization") String token,
                                             @QueryParam("fundNumber") String fundId) {
        Fund result = fundService.getFund(fundId);
        if (result!=null) {
                LOGGER.info("Get an entitled fund detail endpoint hit.");
                return result;
        }
        LOGGER.info("No such entitled fund found.");
//        return ImmutableFund.builder().fundName("").fundNumber("").invCurrency("")
//                .invManager("").moodysRating(0).sAndPRating(0).nav(0).setCycle(0).build();
        return null;
    }

    @POST
    @Path("/addEntitlements")
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response uploadCsvFile(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        int status = serviceUtils.fileUpload(fileInputStream, fileMetaData);
        if (status ==404) {
            LOGGER.info("Wrong file format.");
            return Response.status(status).entity("Provide excel or csv files").build();
        } else if (status == 400) {
            LOGGER.info("File upload failed");
            return Response.status(status).entity("Error while uploading file. Try again").build();
        } else {
            LOGGER.info("File upload successful");
            return serviceUtils.addEntitlementsFromCSV(fileMetaData);
        }
    }
}
