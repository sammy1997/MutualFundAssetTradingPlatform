package com.mutualfundtrading.fundhandling.dao;


import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.models.*;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EntitlementDAO {
    @Autowired
    private FundDAO fundDAO;

    private EntitlementsRepository repository;

    public EntitlementDAO() {
        repository = new EntitlementsRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
    }


    public void insert(String userId, List<String> entitleTo) {
        Optional<Entitlements> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();

        if (entitlementsOptional.isPresent()) {

            ImmutableEntitlements entitlement = ImmutableEntitlements.builder()
                    .from(entitlementsOptional.get()).build();

            HashSet<String> updatedEntitlements = new HashSet<>();

            List<String> entitledFundList = new ArrayList<>();

            for (String entitledFundId: entitlement.entitledTo()) {
                updatedEntitlements.add(entitledFundId);
                entitledFundList.add(entitledFundId);
            }

            for (String newEntitledFundId: entitleTo) {
                if (!updatedEntitlements.contains(newEntitledFundId)) {
                    updatedEntitlements.add(newEntitledFundId);
                    entitledFundList.add(newEntitledFundId);
                }
            }
            repository.upsert(ImmutableEntitlements.builder().userId(userId).entitledTo(entitledFundList).build());
        } else {
            repository.insert(ImmutableEntitlements.builder().userId(userId).entitledTo(entitleTo).build());
        }
    }

    public String delete(String userId, List<String> deleteEntitlements) {
        Optional<Entitlements> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();

        if (entitlementsOptional.isPresent()) {

            List<String> currentEntitlements = new ArrayList<>(entitlementsOptional.get().entitledTo());
            ArrayList<String> entitlementsToDelete = new ArrayList<>(currentEntitlements);

            entitlementsToDelete.retainAll(deleteEntitlements);
            currentEntitlements.removeAll(entitlementsToDelete);

            repository.upsert(ImmutableEntitlements.builder().userId(userId).entitledTo(currentEntitlements).build());
            return "Entitlements deleted";
        }
        return null;
    }

    public List<ImmutableFund> getEntitledFunds(String userId) {
        Optional<Entitlements> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();
        List<ImmutableFund> entitlements = new ArrayList<>();
        if (entitlementsOptional.isPresent()) {
            List<String> entitledFunds = entitlementsOptional.get().entitledTo();

            for (String fundNumber:entitledFunds) {
                ImmutableFund fund = fundDAO.getFund(fundNumber);

                if(fund != null) {
                    entitlements.add(fund);
                }
            }
        }
        return entitlements;
    }

    public List<String> getEntitlementListForUser(String userId){
        Optional<Entitlements> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();

        if (entitlementsOptional.isPresent()) {
            return entitlementsOptional.get().entitledTo();
        }
        return null;
    }

}
