package com.mutualfundtrading.fundhandling.dao;


import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.models.Entitlements;
import com.mutualfundtrading.fundhandling.models.EntitlementsRepository;
import com.mutualfundtrading.fundhandling.models.ImmutableEntitlements;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EntitlementDAO {
    EntitlementsRepository repository;
    EntitlementsRepository.Criteria where;
    public EntitlementDAO(){
        repository = new EntitlementsRepository(RepositorySetup.forUri("mongodb://localhost:27017/FundHandler"));
        where = repository.criteria();
    }

    public void insert(String userId, List<String> entitleTo){
        Optional<Entitlements> entitlementsOptional = repository.findByUserId(userId).fetchFirst().getUnchecked();
        boolean exists = entitlementsOptional.isPresent();
        if (exists){
            ImmutableEntitlements entitlement = ImmutableEntitlements.builder().from(entitlementsOptional.get()).build();
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
            repository.upsert(ImmutableEntitlements.builder().userId(userId).entitledTo(entitledFundList).build());
        }else {
            repository.insert(ImmutableEntitlements.builder().userId(userId).entitledTo(entitleTo).build());
        }
//        return "Added entitlements to " + userId;
    }
}
