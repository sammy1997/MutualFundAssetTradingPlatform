package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

public interface FundControllerModel {
    Response createFund(FundParser fund);
    Fund getFund(FundParser fund);
    List<Fund> getAll();
    Response updateFund(FundParser fund);
    Response delete(@QueryParam("fundNumber") String fundNumber);
    List<Fund> search(@QueryParam("field") String field, @QueryParam("term") String searchTerm);
    List<Fund> getUserEntitlements(@QueryParam("userId") String userId);
    Response uploadCsvFile(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData);
}
