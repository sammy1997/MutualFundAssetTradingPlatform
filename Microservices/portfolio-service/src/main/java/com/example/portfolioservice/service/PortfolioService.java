package com.example.portfolioservice.service;


import com.example.portfolioservice.DAO.UserDAO;
import com.example.portfolioservice.models.*;
import com.example.portfolioservice.models.ImmutableUser;
import com.example.portfolioservice.models.User;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//
@Service
public class PortfolioService
{

    @Autowired
    UserDAO userDAO;



    public String createUser(UserParser userParser)
    {
        return userDAO.createUser(userParser);
    }

    public ImmutableUser getUser(String id)
    {
        return userDAO.getUser(id);
    }

    public Optional<User> update(UserParser userParser)
    {
        return userDAO.update(userParser);
    }

    public Optional<User> delete(String userId)
    {
        return userDAO.delete(userId);
    }


    public BalanceInfo getBalanceById(String userId)
    {
        return userDAO.getBalance(userId);
    }

    public float updateBalance(String userId, float balance)
    {
        return userDAO.updateBalance(userId, balance);
    }

    public String updateBaseCurrency(String userId, String newCurrency)
    {
        return userDAO.updateBaseCurrency(userId, newCurrency);
    }
}
