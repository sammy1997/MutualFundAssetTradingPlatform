package com.mutualfundtrading.fundhandling.dao;

import com.google.common.base.Optional;
import com.jayway.jsonpath.Criteria;
import com.mutualfundtrading.fundhandling.models.*;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FundDAO {
//    Repository and query object setup
    FundDBModelRepository repository;
    FundDBModelRepository.Criteria where;

    public FundDAO(){
        repository = new FundDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
        where = repository.criteria();
    }

//    Insert query method
    public String insert(Fund fund){
        String message = "Successfully added";
        boolean exists = repository.findByFundNumber(fund.fundNumber()).fetchFirst().getUnchecked().isPresent();
        if (exists){
            message = "Fund already exists";
        }else {
            if (fund.invManager().isPresent() && fund.setCycle().isPresent() &&
                    fund.moodysRating().isPresent() && fund.sAndPRating().isPresent() &&
                    fund.fundName().isPresent() && fund.invCurrency().isPresent() && fund.nav().isPresent()) {
//                Constructing the fund
                FundDBModel f = ImmutableFundDBModel.builder()
                        .fundName(fund.fundName().get())
                        .fundNumber(fund.fundNumber())
                        .invCurrency(fund.invCurrency().get())
                        .invManager(fund.invManager().get())
                        .setCycle(fund.setCycle().get())
                        .moodysRating(fund.moodysRating().get())
                        .nav(fund.nav().get())
                        .sAndPRating(fund.sAndPRating().get()).build();
                repository.insert(f);
            }else{
                return "Some fields are missing";
            }
        }
        return message;
    }

//    Fetch fund query
    public ImmutableFundDBModel getFund(String fundNumber){
        Optional<FundDBModel> fund = repository.find(where.fundNumber(fundNumber)).fetchFirst().getUnchecked();
        if (fund.isPresent()){
            ImmutableFundDBModel fund1 = ImmutableFundDBModel.builder().from(fund.get()).build();
            return fund1;
        }
        return null;
    }

//    Fetch all funds query
    public List<ImmutableFundDBModel> getAll(){
        List<FundDBModel> funds = repository.findAll().fetchAll().getUnchecked();
        List<ImmutableFundDBModel> f = new ArrayList<>();
        for (FundDBModel fund: funds){
            f.add(ImmutableFundDBModel.builder().from(fund).build());
        }
        return f;
    }

//    Update fund query
    public Optional<FundDBModel> update(Fund fund){
        Optional<FundDBModel> f = repository.find(where.fundNumber(fund.fundNumber())).fetchFirst().getUnchecked();
        if (f.isPresent()){
            FundDBModel fundCurr = f.get();
            repository.upsert(
                    ImmutableFundDBModel.builder()
                            .fundNumber(fund.fundNumber())
                            .fundName(fund.fundName().isPresent()?fund.fundName().get():fundCurr.fundName())
                            .invCurrency(fund.invCurrency().isPresent()?fund.invCurrency().get():fundCurr.invCurrency())
                            .invManager(fund.invManager().isPresent()?fund.invManager().get():fundCurr.invManager())
                            .moodysRating(fund.moodysRating().isPresent()?fund.moodysRating().get():fundCurr.moodysRating())
                            .nav(fund.nav().isPresent()?fund.nav().get():fundCurr.nav())
                            .setCycle(fund.setCycle().isPresent()?fund.setCycle().get():fundCurr.setCycle())
                            .sAndPRating(fund.sAndPRating().isPresent()?fund.sAndPRating().get():fundCurr.sAndPRating())
                            .build()
            );
        }
        return f;
    }

//    Delete fund
    public Optional<FundDBModel> delete(Fund fund){
        Optional<FundDBModel> f = repository.findByFundNumber(fund.fundNumber()).deleteFirst().getUnchecked();
        return f;
    }

    public List<FundDBModel> searchFundName(String searchTerm){
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNameStartsWith(searchTerm)
                .or().fundNameMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<FundDBModel> searchFundNumber(String searchTerm){
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberStartsWith(searchTerm)
                .or().fundNumberMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<FundDBModel> searchInvCurrency(String searchTerm){
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.invCurrencyStartsWith(searchTerm)
                .or().invCurrencyMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<FundDBModel> searchInvManager(String searchTerm){
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.invManagerStartsWith(searchTerm)
                .or().invManagerMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }
}
