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
    FundDAO fundDAO;

    EntitlementsDBModelRepository repository;
    EntitlementsDBModelRepository.Criteria where;

    public EntitlementDAO(){
        repository = new EntitlementsDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
        where = repository.criteria();
    }


    public void insert(String userId, List<String> entitleTo){
        Optional<EntitlementsDBModel> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();

        if (entitlementsOptional.isPresent()){

            ImmutableEntitlementsDBModel entitlement = ImmutableEntitlementsDBModel.builder()
                    .from(entitlementsOptional.get()).build();

            HashSet<String> updatedEntitlements = new HashSet<>();

            List<String> entitledFundList = new ArrayList<>();

            for (String entitledFundId: entitlement.entitledTo()){
                updatedEntitlements.add(entitledFundId);
                entitledFundList.add(entitledFundId);
            }

            for (String newEntitledFundId: entitleTo){
                if (!updatedEntitlements.contains(newEntitledFundId)){
                    updatedEntitlements.add(newEntitledFundId);
                    entitledFundList.add(newEntitledFundId);
                }
            }
            repository.upsert(ImmutableEntitlementsDBModel.builder().userId(userId).entitledTo(entitledFundList).build());
        }else {
            repository.insert(ImmutableEntitlementsDBModel.builder().userId(userId).entitledTo(entitleTo).build());
        }
    }

    public String delete(String userId, List<String> deleteEntitlements){
        Optional<EntitlementsDBModel> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();
        if (entitlementsOptional.isPresent()){
            List<String> currentEntitlements = new ArrayList<>(entitlementsOptional.get().entitledTo());
            ArrayList<String> entitlementsToDelete = new ArrayList<>(currentEntitlements);

            entitlementsToDelete.retainAll(deleteEntitlements);
            currentEntitlements.removeAll(entitlementsToDelete);
            repository.upsert(ImmutableEntitlementsDBModel.builder().userId(userId).entitledTo(currentEntitlements).build());
            return "Entitlements deleted";
        }
        return null;
    }

    public List<ImmutableFundDBModel> getEntitledFunds(String userId){
        Optional<EntitlementsDBModel> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();
        if (entitlementsOptional.isPresent()){
            List<String> entitledFunds = entitlementsOptional.get().entitledTo();
            List<ImmutableFundDBModel> entitlements = new ArrayList<>();
            for (String fundNumber:entitledFunds){
                ImmutableFundDBModel fund = fundDAO.getFund(fundNumber);
                if(fund!=null){
                    entitlements.add(fund);
                }
            }
            return entitlements;
        }
        return null;
    }

    public List<String> getEntitlementListForUser(String userId){
        Optional<EntitlementsDBModel> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();
        if (entitlementsOptional.isPresent()){
            return entitlementsOptional.get().entitledTo();
        }
        return null;
    }

}
