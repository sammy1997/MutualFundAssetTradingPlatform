package com.mutualfundtrading.fundhandling.services;

import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService {

    @Autowired
    FundDAO dao;

    public String addFundService(Fund fund){
        return dao.insert(fund);
    }

    public ImmutableFundDBModel getFund(String fundNumber){
        return dao.getFund(fundNumber);
    }

    public List<ImmutableFundDBModel> getAll(){
        return dao.getAll();
    }

    public Message update(Fund fund){
        Optional<FundDBModel> f = dao.update(fund);
        if (!f.isPresent()){
            return new Message(404, "Fund with number " + fund.fundNumber() + " not found in db.");
        }
        return new Message(200, "Fund with number " + fund.fundNumber() + " updated.");
    }

    public Message delete(String fundNumber){
        if (dao.delete(fundNumber).isPresent()){
            return new Message(200, "Fund with fund number "+ fundNumber +" deleted");
        }
        return new Message(404, "Fund with fund number "+ fundNumber +" not found.");
    }

    public List<FundDBModel> searchAllFunds(String field, String searchTerm){
//        System.out.println(field);
        if (field.equals("Name")){
            return dao.searchFundName(searchTerm);
        }
        else if (field.equals("Fund Number")){
            return dao.searchFundNumber(searchTerm);
        }
        else if (field.equals("Currency")){
            return dao.searchInvCurrency(searchTerm);
        }else if (field.equals("Manager")){
            return dao.searchInvManager(searchTerm);
        }
        return null;
    }
}
