package com.example.portfolioservice.service;


import com.example.portfolioservice.DAO.UserDAO;
import com.example.portfolioservice.models.ImmutableUserDBModel;
import com.example.portfolioservice.models.User2;
import com.example.portfolioservice.models.UserDBModel;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//
@Service
public class PortfolioService
{

    @Autowired
    UserDAO userDAO;



    public void createUser(User2 user)
    {
        userDAO.createUser(user);
    }

    public ImmutableUserDBModel getUser(String id)
    {
        return userDAO.getUser(id);
    }

    public Optional<UserDBModel> update(User2 user)
    {
        return userDAO.update(user);
    }

    public Optional<UserDBModel> delete(String userId)
    {
        return userDAO.delete(userId);
    }


    public float getBalanceById(String userId)
    {
        return userDAO.getBalance(userId);
    }

    public void updateBalance(String userId, float balance)
    {
        userDAO.updateBalance(userId, balance);
    }
}
