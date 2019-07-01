package com.sapient.usercreateservice.service;

import com.sapient.usercreateservice.dao.UsersDAO;
import com.sapient.usercreateservice.entities.ImmutableUsersDBModel;
import com.sapient.usercreateservice.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;


@Service
public class UserService {




    @Autowired
    private UsersDAO dao;

    public Response addUser(Users users){
        return dao.insert(users);
    }


    public List<ImmutableUsersDBModel> getAllUsers() {
        return dao.getAllUsers();
    }
}
