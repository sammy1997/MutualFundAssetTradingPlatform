package com.mutualfundtrading.fundhandling.services;

import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;

import javax.ws.rs.core.Response;
import java.util.List;

public interface FundServiceModel {
    Response addFundService(FundParser fund);
    Fund getFund(String fundNumber);
    List<Fund> getAll();
    Response update(FundParser fund);
    Response delete(String fundNumber);
    List<Fund> searchAllFunds(String field, String searchTerm);
}
