package com.example.portfolioservice.DAO;


import com.example.portfolioservice.models.Fund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FundDAO
{

    @Autowired
    private FundRepository repository;

    public Collection<Fund> getFunds()
    {
        return repository.findAll();
    }
}
