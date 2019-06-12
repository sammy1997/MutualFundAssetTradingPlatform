package com.mutualfundtrading.fundhandling.dao;

import com.google.common.base.Optional;
import com.jayway.jsonpath.Criteria;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundRepository;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;

public class FundDAO {
    FundRepository repository;
    FundRepository.Criteria where;
    public FundDAO(){
        repository = new FundRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
        where = repository.criteria();
    }

    public String insert(String fundName, String fundNumber, String invManager, int setCycle,
                         float nav, String invCurrency, float sAndPRating, float moodysRating){
        String message = "Successfully added";
        boolean exists = repository.findByFundNumber(fundNumber).fetchFirst().getUnchecked().isPresent();
        if (exists){
            message = "Fund already exists";
        }else {
            Fund f = ImmutableFund.builder()
                    .fundName(fundName)
                    .fundNumber(fundNumber)
                    .invManager(invManager)
                    .setCycle(setCycle)
                    .nav(nav)
                    .invCurrency(invCurrency)
                    .sAndPRating(sAndPRating)
                    .moodysRating(moodysRating)
                    .build();
            repository.insert(f);
        }
        return message;
    }

    public ImmutableFund getFund(String fundNumber){
        Optional<Fund> fund = repository.find(where.fundNumber(fundNumber)).fetchFirst().getUnchecked();
        if (fund.isPresent()){
            ImmutableFund fund1 = ImmutableFund.builder().from(fund.get()).build();
            System.out.println(fund1.fundName());
            return fund1;
        }
        return null;
    }

    public List<ImmutableFund> getAll(){
        List<Fund> funds = repository.findAll().fetchAll().getUnchecked();
        List<ImmutableFund> f = new ArrayList<ImmutableFund>();
        for (Fund fund: funds){
            f.add(ImmutableFund.builder().from(fund).build());
        }
        return f;
    }
}
