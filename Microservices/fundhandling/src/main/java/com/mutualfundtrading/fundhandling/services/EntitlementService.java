package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;

import com.mutualfundtrading.fundhandling.models.EntitlementParser;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntitlementService {
    @Autowired
    private EntitlementDAO dao;

    @Autowired
    private FundDAO fundDAO;

    @Autowired
    private FundService fundService;

    // Add entitlements
    public Response addEntitlements(EntitlementParser entitlement) {
        if (entitlement.entitledTo().isPresent()) {
            List<String> temp = new ArrayList<>();
            for (String fundId : entitlement.entitledTo().get()) {
                if (fundService.getFund(fundId) != null) {
                    temp.add(fundId);
                }
            }
            if (temp.size() == 0) {
                return Response.status(404).entity("None of the funds exists in the database").build();
            }

            dao.insert(entitlement.userId().get(), temp);

            if (temp.size() < entitlement.entitledTo().get().size()) {
                return Response.status(200)
                        .entity("Some of the funds were not found in the database. Remaining were added").build();
            }
            return Response.status(200).entity("All entitlements added").build();
        } else {
            return Response.status(404).entity("Fund list cannot be empty").build();
        }
    }

    // Delete entitlements
    public Response deleteEntitlements(EntitlementParser entitlement) {
        if (entitlement.entitledTo().isPresent() && entitlement.entitledTo().get().size()>0) {
            String message = dao.delete(entitlement.userId().get(), entitlement.entitledTo().get());
            if (message == null) {
                return Response.status(404).entity("User with user ID " + entitlement.userId()
                        .get() + " not found.").build();
            }
            return Response.status(200)
                    .entity("The required entitlements for user "+ entitlement.userId()
                            .get() +" have been deleted.").build();
        } else {
            return  Response.status(404).entity("Fund list cannot be empty").build();
        }
    }

    // Fetch entitlements
    public List<ImmutableFund> getEntitlements(String userId) {
        return dao.getEntitledFunds(userId);
    }

    // Search entitlements
    public List<Fund> searchEntitlements(String userId, String field, String searchTerm) {
        List<String> entitlements = dao.getEntitlementListForUser(userId);
        if (entitlements != null) {
            if (field.equals("Name")) {
                return fundDAO.searchFundNameInEntitlements(searchTerm, entitlements);
            } else if (field.equals("Fund Number")) {
                return fundDAO.searchFundNumberInEntitlements(searchTerm, entitlements);
            } else if (field.equals("Currency")) {
                return fundDAO.searchInvCurrencyInEntitlements(searchTerm, entitlements);
            } else if (field.equals("Manager")) {
                return fundDAO.searchInvManagerInEntitlements(searchTerm, entitlements);
            }
            return null;
        }
        return null;
    }
}
