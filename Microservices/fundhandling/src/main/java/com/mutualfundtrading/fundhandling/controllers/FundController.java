package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
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
    public Response createFund(Fund fund){
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
    public List<ImmutableFundDBModel> getAll(){
        return service.getAll();
    }

//    Update fund details
    @Path("/update")
    @PATCH
    public Response updateFund(Fund fund){
        return service.update(fund);
    }

//    Delete funds
    @Path("/delete")
    @DELETE
    public Response delete(@QueryParam("fundNumber") String fundNumber){
        return service.delete(fundNumber);
    }

//    Search funds
    @Path("/search")
    @GET
    public List<FundDBModel> search(@QueryParam("field") String field, @QueryParam("term") String searchTerm){
        return service.searchAllFunds(field, searchTerm);
    }

//    Add from CSV. TODO

    @POST
    @Path("/addFund")
    @Consumes({MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response uploadCsvFile(  @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
    {
        String UPLOAD_PATH = "C:\\Users\\somchakr\\Desktop\\upload\\";
        try
        {
            int read = 0;
            byte[] bytes = new byte[1024];
            String fileName = fileMetaData.getFileName();
            if (!(fileName.contains("xlx") && fileName.contains("xlsx")  && fileName.contains("csv"))){
                return Response.status(404).entity("Provide files").build();
            }
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileName));
//            System.out.println(fileMetaData.getFileName());

            while ((read = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }
        return Response.ok("Data uploaded successfully !!").build();
    }
}
