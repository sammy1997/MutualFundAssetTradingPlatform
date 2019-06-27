package com.example.portfolioservice.DAO;

import com.example.portfolioservice.models.*;
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
                .userId(user.userId().get())
                .baseCurr(user.baseCurr().get())
                .balance(user.balance())
                .all_funds(user.all_funds().get()).build();

        repository.insert(userDB);

    }

    public ImmutableUserDBModel getUser(String id)
    {
        Optional<UserDBModel> user =repository.find(where.userId(id)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            return (ImmutableUserDBModel) user.get();
        }
        return null;
    }

    public Optional<UserDBModel> update(User2 user)
    {
        Optional<UserDBModel> userDB = repository.find(where.userId(user.userId().get())).fetchFirst().getUnchecked();
        if(userDB.isPresent())
        {
            UserDBModel updated_user = userDB.get();
            repository.upsert(
                    ImmutableUserDBModel.builder()
                    .userId(updated_user.userId()).balance(user.balance())
                            .baseCurr(user.baseCurr().isPresent()? user.baseCurr().get(): updated_user.baseCurr())
                            .all_funds(user.all_funds().isPresent()? user.all_funds().get():updated_user.all_funds())
                    .build()
            );
        }
        return userDB;
    }

    public Optional<UserDBModel> delete(String userId)
    {
        return repository.findByUserId(userId).deleteFirst().getUnchecked();
    }

    public BalanceInfo getBalance(String userId)
    {
        Optional<UserDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            BalanceInfo balance = new BalanceInfo();
            balance.setBalance(user.get().balance());
            balance.setBaseCurr(user.get().baseCurr());
            return balance;
        }
        return null;
    }

    public void updateBalance(String userId, float balance)
    {
        Optional<UserDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            repository.upsert(
                    ImmutableUserDBModel.builder()
                            .userId(user.get().userId())
                            .all_funds(user.get().all_funds())
                            .balance(balance)
                            .build()
            );
        }
    }
}
