package com.mutualfundtrading.fundhandling.dao;

import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.models.*;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FundDAO {

    private FundRepository repository;
    private FundRepository.Criteria where;

    public FundDAO() {
        repository = new FundRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
        where = repository.criteria();
    }

    // Insert query method
    public boolean insert(FundParser fund) {
        boolean exists = repository.findByFundNumber(fund.fundNumber()).fetchFirst().getUnchecked().isPresent();
        if (exists) {
            return false;
        } else {
                // Constructing the fund
                Fund f = ImmutableFund.builder()
                        .fundName(fund.fundName().get())
                        .fundNumber(fund.fundNumber())
                        .invCurrency(fund.invCurrency().get())
                        .invManager(fund.invManager().get())
                        .setCycle(fund.setCycle().get())
                        .moodysRating(fund.moodysRating().get())
                        .nav(fund.nav().get())
                        .sAndPRating(fund.sAndPRating().get()).build();
                repository.insert(f);
            }
        return true;
    }

    // Fetch fund query
    public Fund getFund(String fundNumber) {
        Optional<Fund> fund = repository.find(where.fundNumber(fundNumber)).fetchFirst().getUnchecked();
        if (fund.isPresent()) {
            return ImmutableFund.builder().from(fund.get()).build();
        }
        return null;
    }

    // Fetch all funds query
    public List<Fund> getAll() {
        List<Fund> funds = repository.findAll().fetchAll().getUnchecked();
        return funds;
    }

    // Update fund query
    public Optional<Fund> update(FundParser fund) {
        Optional<Fund> f = repository.find(where.fundNumber(fund.fundNumber())).fetchFirst().getUnchecked();
        if (f.isPresent()){
            Fund fundCurr = f.get();
            repository.upsert(
                    ImmutableFund.builder()
                            .fundNumber(fund.fundNumber())
                            .fundName(fund.fundName().isPresent() ? fund.fundName().get() : fundCurr.fundName())
                            .invCurrency(fund.invCurrency().isPresent() ? fund.invCurrency().get() : fundCurr.invCurrency())
                            .invManager(fund.invManager().isPresent() ? fund.invManager().get() : fundCurr.invManager())
                            .moodysRating(fund.moodysRating().isPresent() ? fund.moodysRating().get() : fundCurr.moodysRating())
                            .nav(fund.nav().isPresent() ? fund.nav().get(): fundCurr.nav())
                            .setCycle(fund.setCycle().isPresent() ? fund.setCycle().get() : fundCurr.setCycle())
                            .sAndPRating(fund.sAndPRating().isPresent()? fund.sAndPRating().get() : fundCurr.sAndPRating())
                            .build()
            );
        }
        return f;
    }

    // Delete fund
    public Optional<Fund> delete(String fundNumber) {
        Optional<Fund> f = repository.findByFundNumber(fundNumber).deleteFirst().getUnchecked();
        return f;
    }

    public List<Fund> searchFundName(String searchTerm) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNameStartsWith(searchTerm)
                .or().fundNameMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<Fund> searchFundNumber(String searchTerm) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberStartsWith(searchTerm)
                .or().fundNumberMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<Fund> searchInvCurrency(String searchTerm) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.invCurrencyStartsWith(searchTerm)
                .or().invCurrencyMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }


    public List<Fund> searchInvManager(String searchTerm) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.invManagerStartsWith(searchTerm)
                .or().invManagerMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }

    public List<Fund> searchFundNameInEntitlements(String searchTerm, List<String> entitlements) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberIn(entitlements).fundNameStartsWith(searchTerm)
                .or().fundNumberIn(entitlements).fundNameMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }

    public List<Fund> searchFundNumberInEntitlements(String searchTerm, List<String> entitlements) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberIn(entitlements).fundNumberStartsWith(searchTerm)
                .or().fundNumberIn(entitlements).fundNumberMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }

    public List<Fund> searchInvCurrencyInEntitlements(String searchTerm, List<String> entitlements) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberIn(entitlements).invCurrencyStartsWith(searchTerm)
                .or().fundNumberIn(entitlements).invCurrencyMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }

    public List<Fund> searchInvManagerInEntitlements(String searchTerm, List<String> entitlements) {
        String pattern = ".*" + searchTerm + ".*";
        return repository.find(where.fundNumberIn(entitlements).invManagerStartsWith(searchTerm)
                .or().fundNumberIn(entitlements).invManagerMatches(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)))
                .fetchAll().getUnchecked();
    }
}
