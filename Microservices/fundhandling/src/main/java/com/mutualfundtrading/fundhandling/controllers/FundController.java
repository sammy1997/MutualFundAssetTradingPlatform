package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.services.EntitlementService;
import com.mutualfundtrading.fundhandling.services.FundService;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import org.slf4j.Logger;


@Path("/funds")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class FundController {
    @Autowired
    private FundService service;

    @Autowired
    private EntitlementService entitlementService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FundController.class);

    // Fund creation url
    @Path("/create")
    @POST
    public Response createFund(FundParser fund) {
        LOGGER.info("Create fund endpoint hit.");
        return service.addFundService(fund);
    }

    // Fund details update URL
    @Path("/retrieve")
    @POST
    public ImmutableFund getFund(FundParser fund) {
        LOGGER.info("Get fund endpoint hit.");
        return service.getFund(fund.fundNumber());
    }

    // Get all funds
    @Path("/all")
    @GET
    public List<ImmutableFund> getAll() {
        LOGGER.info("Get all funds endpoint hit.");
        return service.getAll();
    }

    // Update fund details
    @Path("/update")
    @PATCH
    public Response updateFund(FundParser fund) {
        LOGGER.info("Update fund endpoint hit.");
        return service.update(fund);
    }

    // Delete funds
    @Path("/delete")
    @DELETE
    public Response delete(@QueryParam("fundNumber") String fundNumber) {
        LOGGER.info("Delete fund endpoint hit.");
        return service.delete(fundNumber);
    }

    // Search funds
    @Path("/search")
    @GET
    public List<Fund> search(@QueryParam("field") String field, @QueryParam("term") String searchTerm) {
        LOGGER.info("Search fund endpoint hit");
        return service.searchAllFunds(field, searchTerm);
    }

    // Fetch entitled funds of a user on admin request
    @Path("/entitlements")
    @GET
    public List<ImmutableFund> getUserEntitlements(@QueryParam("userId") String userId){
        LOGGER.info("Get entitlements request from admin");
        return entitlementService.getEntitlements(userId);
    }

    // Add from CSV file
    @POST
    @Path("/addFund")
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response uploadCsvFile(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        int status = ServiceUtils.fileUpload(fileInputStream, fileMetaData);
        if (status == 404) {
            LOGGER.info("Wrong file format.");
            return Response.status(status).entity("Provide excel or csv files").build();
        } else if (status == 400) {
            LOGGER.info("File upload failed");
            return Response.status(status).entity("Error while uploading file. Try again").build();
        } else {
            LOGGER.info("File upload successful");
            return ServiceUtils.addFundsFromCSV(fileMetaData.getFileName());
        }
    }
}
