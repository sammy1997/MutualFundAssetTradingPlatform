package com.sapient.usercreateservice.dao;


import com.sapient.usercreateservice.entities.ImmutableUser;

import com.sapient.usercreateservice.entities.ParsedUser;
import com.sapient.usercreateservice.entities.User;
import com.sapient.usercreateservice.entities.UserRepository;

import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    UserRepository repository;
    UserRepository.Criteria where;

    public UserDAO(){
        repository = new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/UsersList"));
        where = repository.criteria();
    }

    public Response insert(ParsedUser user){
        boolean exists = repository.findByUserId(user.userId()).fetchFirst().getUnchecked().isPresent();
        if(exists){
            return Response.status(406).entity("Not Acceptable").build();
        }else{
            if(user.fullName().isPresent() && user.currBal().isPresent() && user.role().isPresent()){
                User currUser = ImmutableUser.builder()
                        .userId(user.userId())
                        .password(encoder.encode(user.password()))
                        .fullName(user.fullName().get())
                        .role(user.role().get()).build();
                repository.insert(currUser);
                return Response.status(200).entity("OK").build();
            }else{
                return Response.status(400).entity("Bad Request").build();
            }
        }
    }

    public List<ImmutableUser> getAllUsers() {
        List<User> users = repository.findAll().fetchAll().getUnchecked();
        List<ImmutableUser> tempList = new ArrayList<>();
        for (User allUsers: users){
            tempList.add(ImmutableUser.builder().from(allUsers).build());
        }
        return tempList;
    }
}
