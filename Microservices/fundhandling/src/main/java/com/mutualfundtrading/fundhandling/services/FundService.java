package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService {
    FundDAO dao = new FundDAO();

    public String addFundService(String fundName, String fundNumber, String invManager, int setCycle,
                                 float nav, String invCurrency, float sAndPRating, float moodysRating){
        return dao.insert(fundName, fundNumber, invManager, setCycle, nav, invCurrency, sAndPRating, moodysRating);
    }

    public ImmutableFund getFund(String fundNumber){
        return dao.getFund(fundNumber);
    }

    public List<ImmutableFund> getAll(){
        return dao.getAll();
    }
}
