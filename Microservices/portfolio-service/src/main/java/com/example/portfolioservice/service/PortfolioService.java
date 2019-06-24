package com.example.portfolioservice.service;


import java.util.*;

import com.example.portfolioservice.DAO.FundDAO;
import com.example.portfolioservice.DAO.UserDAO;
import com.example.portfolioservice.models.Fund;
import com.example.portfolioservice.models.User;
import com.example.portfolioservice.models.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//
@Service
public class PortfolioService
{
    @Autowired
    private FundDAO fundDAO;

    @Autowired
    private UserDAO userDAO;

    public Collection<Fund> getFunds()
    {
        return fundDAO.getFunds();
    }

    public Collection<User> getUser()
    {
        return userDAO.getUsers();
    }

    public User createUser(User user)
    {
         userDAO.createUser(user);
         return user;
    }

    public Optional<User> getUserById(String id)
    {
        return userDAO.getUserById(id);
    }

    public Optional<User> deleteUserById(String id)
    {
        return userDAO.deleteUserById(id);
    }

    public Optional<User> updateUserById(String userId, UserUpdate userUpdate)
    {
        return userDAO.updateUser(userId, userUpdate);
    }
}
