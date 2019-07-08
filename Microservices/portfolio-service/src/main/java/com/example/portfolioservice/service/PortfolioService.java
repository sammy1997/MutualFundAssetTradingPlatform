package com.example.portfolioservice.service;


import com.example.portfolioservice.DAO.UserDAO;
import com.example.portfolioservice.models.BalanceInfo;
import com.example.portfolioservice.models.UserParser;
import com.example.portfolioservice.models.User;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@SuppressWarnings("ALL")
@Service
public class PortfolioService {
    @Autowired
    private UserDAO userDAO;

    public String createUser(final UserParser userParser) {
        return userDAO.createUser(userParser);
    }

    public Optional<User> getUser(final String id) {
        return userDAO.getUser(id);
    }

    public Optional<User> update(final UserParser userParser) {
        return userDAO.update(userParser);
    }

    public Optional<User> delete(final String userId) {
        return userDAO.delete(userId);
    }


    public Optional<BalanceInfo> getBalanceById(final String userId) {
        return userDAO.getBalance(userId);
    }

    public float updateBalance(final String userId, final float balance) {
        return userDAO.updateBalance(userId, balance);
    }

    public String updateBaseCurrency(final String userId, final String newCurrency) {
        return userDAO.updateBaseCurrency(userId, newCurrency);
    }
}
