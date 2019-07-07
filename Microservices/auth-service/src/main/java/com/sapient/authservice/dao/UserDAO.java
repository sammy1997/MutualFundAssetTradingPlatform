package com.sapient.authservice.dao;

import com.google.common.base.Optional;
import com.sapient.authservice.entities.User;
import com.sapient.authservice.entities.UserRepository;
import com.sapient.authservice.entities.ImmutableUser;
import org.immutables.mongo.repository.RepositorySetup;

public class UserDAO {
    UserRepository repository;
    UserRepository.Criteria where;

    public UserDAO(){
        repository = new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/UsersList"));
        where = repository.criteria();
    }

    public ImmutableUser getUserByUserId(String userId){
        Optional<User> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent()){
            ImmutableUser validUser = ImmutableUser.builder().from(user.get()).build();
            return validUser;
        }
        return null;
    }
}
