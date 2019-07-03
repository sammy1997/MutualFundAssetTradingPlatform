package com.mutualfundtrading.fundhandling.services;

import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
public class FundService {

    @Autowired
    FundDAO dao;

    public Response addFundService(Fund fund){
        String message = dao.insert(fund);
        if (message.equals("Successfully added")){
            return Response.status(200).entity(message).build();
        }
        if(message.equals("Some fields are missing")){
            return Response.status(400).entity(message).build();
        }
        return Response.status(422).entity(message).build();
    }

    public ImmutableFundDBModel getFund(String fundNumber){
        return dao.getFund(fundNumber);
    }

    public List<ImmutableFundDBModel> getAll(){
        return dao.getAll();
    }

    public Response update(Fund fund){
        Optional<FundDBModel> f = dao.update(fund);
        if (!f.isPresent()){
            return Response.status(404).entity("Fund with number " + fund.fundNumber() + " not found in db.").build();
        }
        return Response.status(200).entity("Fund with number " + fund.fundNumber() + " updated.").build();
    }

    public Response delete(String fundNumber){
        if (dao.delete(fundNumber).isPresent()){
            return Response.status(200).entity("Fund with fund number "+ fundNumber +" deleted").build();
        }
        return Response.status(404).entity("Fund with fund number "+ fundNumber +" not found.").build();
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
