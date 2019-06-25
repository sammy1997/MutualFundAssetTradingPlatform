package com.example.portfolioservice.DAO;

import com.example.portfolioservice.models.ImmutableUserDBModel;
import com.example.portfolioservice.models.User2;
import com.example.portfolioservice.models.UserDBModel;
import com.example.portfolioservice.models.UserDBModelRepository;
import com.google.common.base.Optional;
import org.immutables.mongo.repository.RepositorySetup;


public class UserDAO
{

    private UserDBModelRepository repository;
    private UserDBModelRepository.Criteria where;



    public UserDAO()
    {
        repository =new UserDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/portfolio"));
        where = repository.criteria();
    }


    public void createUser(User2 user)
    {
        UserDBModel userDB;
        userDB = ImmutableUserDBModel.builder()
                .userId(user.userId())
                .balance(user.balance())
                .all_funds(user.all_funds().get()).build();

        repository.insert(userDB);

    }

    public ImmutableUserDBModel getUser(String id)
    {
        Optional<UserDBModel> user =repository.find(where.userId(id)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            ImmutableUserDBModel userd =ImmutableUserDBModel.builder().from(user.get()).build();
            return userd;
        }
        return null;
    }

    public Optional<UserDBModel> update(User2 user)
    {
        Optional<UserDBModel> u = repository.find(where.userId(user.userId())).fetchFirst().getUnchecked();
        if(u.isPresent())
        {
            UserDBModel updated_user = u.get();
            repository.upsert(
                    ImmutableUserDBModel.builder()
                    .userId(user.userId())
                    .all_funds(user.all_funds().isPresent()?user.all_funds().get():updated_user.all_funds())
                    .build()
            );
        }
        return u;
    }

    public Optional<UserDBModel> delete(String userId)
    {
        Optional<UserDBModel> u = repository.findByUserId(userId).deleteFirst().getUnchecked();
        return u;
    }

    public float getBalance(String userId)
    {
        Optional<UserDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            ImmutableUserDBModel userd =ImmutableUserDBModel.builder().from(user.get()).build();
            return userd.balance();
        }
        return 0;
    }

    public void updateBalance(String userId, float balance)
    {
        Optional<UserDBModel> u = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(u.isPresent())
        {
            UserDBModel updated_user = u.get();
            repository.upsert(
                    ImmutableUserDBModel.builder()
                            .userId(u.get().userId())
                            .all_funds(u.get().all_funds())
                            .balance(balance)
                            .build()
            );
        }
    }
}
