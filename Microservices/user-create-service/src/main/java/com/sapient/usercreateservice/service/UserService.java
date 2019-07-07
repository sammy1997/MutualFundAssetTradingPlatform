package com.sapient.usercreateservice.service;

import com.sapient.usercreateservice.dao.UserDAO;
import com.sapient.usercreateservice.entities.ImmutableUser;
import com.sapient.usercreateservice.entities.ParsedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserDAO dao;

    public Response addUser(ParsedUser users){
        return dao.insert(users);
    }


    public List<ImmutableUser> getAllUsers() {
        return dao.getAllUsers();
    }
}
