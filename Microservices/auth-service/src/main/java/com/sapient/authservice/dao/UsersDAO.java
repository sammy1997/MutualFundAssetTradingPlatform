package com.sapient.authservice.dao;

import com.google.common.base.Optional;
import com.sapient.authservice.entities.ImmutableUsersDBModel;
import com.sapient.authservice.entities.Users;
import com.sapient.authservice.entities.UsersDBModel;
import com.sapient.authservice.entities.UsersDBModelRepository;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersDAO {
    UsersDBModelRepository repository;
    UsersDBModelRepository.Criteria where;

    public UsersDAO(){
        repository = new UsersDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/UsersList"));
        where = repository.criteria();
    }

    public ImmutableUsersDBModel getUserByUserId(String userId){
        Optional<UsersDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent()){
            ImmutableUsersDBModel validUser = ImmutableUsersDBModel.builder().from(user.get()).build();
            return validUser;
        }
        return null;
    }
}
