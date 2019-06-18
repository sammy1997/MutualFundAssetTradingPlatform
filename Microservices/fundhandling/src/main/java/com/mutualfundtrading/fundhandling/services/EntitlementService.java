package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
//import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Entitlements;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class EntitlementService {
    @Autowired
    EntitlementDAO dao;

    @Autowired
    FundDAO fundDAO;

    @Autowired
    FundService fundService;

//    Add entitlements
    public Message addEntitlements(Entitlements entitlement){
        if (entitlement.entitledTo().isPresent()) {
            List<String> temp = new ArrayList<>();
            for (String fundId : entitlement.entitledTo().get()) {
                if (fundService.getFund(fundId) != null) {
                    temp.add(fundId);
                }
            }
            if (temp.size() == 0) {
                return new Message(404, "None of the funds exists in the database");
            }

            dao.insert(entitlement.userId(), temp);

            if (temp.size() < entitlement.entitledTo().get().size()) {
                return new Message(200, "Some of the funds were not found in the database. Remaining were added");
            }

            return new Message(200, "All entitlements added");
        }else {
            return new Message(404, "Fund list cannot be empty");
        }
    }

//    Delete entitlements
    public Message deleteEntitlements(Entitlements entitlement){
        if (entitlement.entitledTo().isPresent()){
            String message = dao.delete(entitlement.userId(), entitlement.entitledTo().get());
            if (message == null){
                return new Message(404, "User with user ID " + entitlement.userId() + " not found.");
            }
            return new Message(200, "The required entitlements for user "+ entitlement.userId() +" have been deleted.");
        }else {
            return  new Message(404, "Fund list cannot be empty");
        }
    }

//    Fetch entitlements
    public List<ImmutableFundDBModel> getEntitlements(String userId){
        return dao.getEntitledFunds(userId);
    }

//    Search entitlements
    public List<FundDBModel> searchEntitlements(String userId, String field, String searchTerm){
        List<String> entitlements = dao.getEntitlementListForUser(userId);
        if (entitlements != null){
            if (field.equals("Name")){
                return fundDAO.searchFundNameInEntitlements(searchTerm, entitlements);
            }
            else if (field.equals("Fund Number")){
                return fundDAO.searchFundNumberInEntitlements(searchTerm, entitlements);
            }
            else if (field.equals("Currency")){
                return fundDAO.searchInvCurrencyInEntitlements(searchTerm, entitlements);
            }else if (field.equals("Manager")){
                return fundDAO.searchInvManagerInEntitlements(searchTerm, entitlements);
            }
            return null;
        }
        return null;
    }
}
