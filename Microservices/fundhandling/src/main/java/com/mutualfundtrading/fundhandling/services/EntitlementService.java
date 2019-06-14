package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
//import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
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
    FundService fundService;

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
            return new Message(200, "Some of the entitlements were not found in the database. Remaining were added");
        }

        return new Message(200, "All entitlements added");
    }
}
