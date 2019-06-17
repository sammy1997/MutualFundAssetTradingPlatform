package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
    public Message addEntitlements(String userId, List<String> entitleTo){
        List<String> temp = new ArrayList<>();
        for (String fundId:entitleTo){
            if (fundService.getFund(fundId) != null){
                temp.add(fundId);
            }
        }
        if (temp.size() == 0){
            return new Message(404, "None of the funds exists in the database");
        }

        dao.insert(userId, temp);

        if ( temp.size() < entitleTo.size()){
            return new Message(200, "Some of the funds were not found in the database. Remaining were added");
        }

        return new Message(200, "All entitlements added");
    }

//    Delete entitlements
    public Message deleteEntitlements(String userId, List<String> entitlementsToDelete){
        String message = dao.delete(userId, entitlementsToDelete);
        if (message == null){
            return new Message(404, "User with user ID " + userId + " not found.");
        }
        return new Message(200, "The required entitlements for user "+ userId +" have been deleted.");
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
