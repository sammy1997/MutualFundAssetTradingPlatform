package com.mutualfundtrading.fundhandling.services;

import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
public class FundService implements FundServiceModel{

    @Autowired
    private FundDAO dao;

    public Response addFundService(FundParser fund) {

        if (!fund.fundNumber().equals("") && fund.invManager().isPresent() && fund.setCycle().isPresent()
                && fund.moodysRating().isPresent() && fund.sAndPRating().isPresent()
                && fund.fundName().isPresent() && fund.invCurrency().isPresent() && fund.nav().isPresent()) {

            boolean status = dao.insert(fund);
            if (status) {
                return Response.status(200).entity("Successfully added").build();
            }
            return Response.status(422).entity("Fund already exists").build();
        }

        return Response.status(400).entity("Some fields are missing").build();
    }

    public Fund getFund(String fundNumber) {
        return dao.getFund(fundNumber);
    }

    public List<Fund> getAll() {
        return dao.getAll();
    }

    public Response update(FundParser fund) {
        Optional<Fund> f = dao.update(fund);
        if (!f.isPresent()) {
            return Response.status(404)
                    .entity("Fund with number " + fund.fundNumber() + " not found in db.").build();
        }
        return Response.status(200)
                .entity("Fund with number " + fund.fundNumber() + " updated.").build();
    }

    public Response delete(String fundNumber) {
        if (dao.delete(fundNumber).isPresent()) {
            return Response.status(200)
                    .entity("Fund with fund number " + fundNumber + " deleted").build();
        }
        return Response.status(404)
                .entity("Fund with fund number " + fundNumber +  " not found.").build();
    }

    public List<Fund> searchAllFunds(String field, String searchTerm) {
        //  System.out.println(field);
        if (field.equals("Name")) {
            return dao.searchFundName(searchTerm);
        } else if (field.equals("Fund Number")) {
            return dao.searchFundNumber(searchTerm);
        } else if (field.equals("Currency")) {
            return dao.searchInvCurrency(searchTerm);
        } else if (field.equals("Manager")) {
            return dao.searchInvManager(searchTerm);
        }
        return null;
    }

}
