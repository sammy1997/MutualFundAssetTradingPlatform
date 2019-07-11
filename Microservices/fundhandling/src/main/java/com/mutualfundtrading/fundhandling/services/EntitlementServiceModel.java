package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.models.EntitlementParser;
import com.mutualfundtrading.fundhandling.models.Fund;

import javax.ws.rs.core.Response;
import java.util.List;

public interface EntitlementServiceModel {
    Response addEntitlements(EntitlementParser entitlement, String token);
    Response updateEntitlements(EntitlementParser entitlements);
    Response deleteEntitlements(EntitlementParser entitlement);
    List<Fund> getEntitlements(String userId);
    List<Fund> searchEntitlements(String userId, String field, String searchTerm);
}
