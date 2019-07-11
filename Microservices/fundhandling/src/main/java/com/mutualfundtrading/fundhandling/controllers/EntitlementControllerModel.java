package com.mutualfundtrading.fundhandling.controllers;

import com.mutualfundtrading.fundhandling.models.EntitlementParser;
import com.mutualfundtrading.fundhandling.models.Fund;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

public interface EntitlementControllerModel {
    Response createEntitlement(@HeaderParam("Authorization") String token, EntitlementParser entitlement);
    Response updateEntitlements(EntitlementParser entitlement);
    Response deleteEntitlements(EntitlementParser entitlement, @HeaderParam("Authorization") String token);
    List<Fund> getEntitlements(@HeaderParam("Authorization") String token);
    List<Fund> searchEntitlements(@HeaderParam("Authorization") String token,
                                  @QueryParam("field") String field,
                                  @QueryParam("term") String searchTerm);
    Fund getEntitledFundDetail(@HeaderParam("Authorization") String token,
                               @QueryParam("fundNumber") String fundId);
    Response uploadCsvFile(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData);
}
