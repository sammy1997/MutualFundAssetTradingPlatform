package com.sapient.usercreateservice.service;

import com.sapient.usercreateservice.dao.UsersDAO;
import com.sapient.usercreateservice.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {




    @Autowired
    private UsersDAO dao;

    public void addUser(Users users){
        dao.insert(users);
    }




}
