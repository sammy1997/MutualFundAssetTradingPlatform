package com.sapient.usercreateservice.dao;


import com.sapient.usercreateservice.entities.ImmutableUsersDBModel;
import com.sapient.usercreateservice.entities.Users;
import com.sapient.usercreateservice.entities.UsersDBModel;
import com.sapient.usercreateservice.entities.UsersDBModelRepository;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.ws.rs.core.Response;

public class UsersDAO {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    UsersDBModelRepository repository;
    UsersDBModelRepository.Criteria where;

    public UsersDAO(){
        repository = new UsersDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/UsersList"));
        where = repository.criteria();
    }

    public Response insert(Users user){
        boolean exists = repository.findByUserId(user.userId()).fetchFirst().getUnchecked().isPresent();
        if(exists){
            return Response.status(406).entity("Not Acceptable").build();
        }else{
            if(user.fullName().isPresent() && user.currBal().isPresent() && user.role().isPresent()){
                UsersDBModel usersDBModel = ImmutableUsersDBModel.builder()
                        .userId(user.userId())
                        .password(encoder.encode(user.password()))
                        .fullName(user.fullName().get())
                        .role(user.role().get()).build();
                repository.insert(usersDBModel);
                return Response.status(200).entity("OK").build();
            }else{
                return Response.status(400).entity("Bad Request").build();
            }
        }
    }
}
