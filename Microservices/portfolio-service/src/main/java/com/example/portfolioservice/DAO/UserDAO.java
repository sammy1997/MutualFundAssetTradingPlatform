package com.example.portfolioservice.DAO;

import java.util.*;

import com.example.portfolioservice.models.User;
import com.example.portfolioservice.models.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDAO
{
    @Autowired
    private UserRepository userRepository;

    public Collection<User> getUsers()
    {
        return userRepository.findAll();
    }


    public User createUser(User user)
    {
        userRepository.insert(user);
        return user;
    }

    public Optional<User> getUserById(String id)
    {
        //userRepository.deleteAll();
        return userRepository.findById(id);
    }
    public Optional<User> deleteUserById(String id)
    {
        //userRepository.deleteAll();
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> userRepository.delete(u));
        return user;
    }

    public Optional<User> updateUser(String userId, UserUpdate userUpdate)
    {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> u.setAll_funds(userUpdate.getAll_funds()));
        user.ifPresent(u -> userRepository.save(u));
        return user;
    }
}
