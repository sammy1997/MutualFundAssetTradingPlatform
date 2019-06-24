package com.sapient.usercreateservice.dao;


import com.sapient.usercreateservice.entities.ImmutableUsersDBModel;
import com.sapient.usercreateservice.entities.Users;
import com.sapient.usercreateservice.entities.UsersDBModel;
import com.sapient.usercreateservice.entities.UsersDBModelRepository;
import org.immutables.mongo.repository.RepositorySetup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsersDAO {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    UsersDBModelRepository repository;
    UsersDBModelRepository.Criteria where;

    public UsersDAO(){
        repository = new UsersDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/UsersList"));
        where = repository.criteria();
    }

    public void insert(Users user){
        boolean exists = repository.findByUsername(user.userId()).fetchFirst().getUnchecked().isPresent();
        if(exists){
            System.out.println("User already exists");
        }else{
            if(user.password().isPresent() && user.fullName().isPresent() && user.currBal().isPresent() && user.baseCurr().isPresent() && user.role().isPresent()){
                UsersDBModel usersDBModel = ImmutableUsersDBModel.builder()
                        .username(user.userId())
                        .password(encoder.encode(user.password().get()))
                        .fullName(user.fullName().get())
                        .currBal(user.currBal().get())
                        .baseCurr(user.baseCurr().get())
                        .role(user.role().get()).build();
                repository.insert(usersDBModel);
            }else{
                System.out.println("Some field(s) are missing");
            }
        }
    }
}
