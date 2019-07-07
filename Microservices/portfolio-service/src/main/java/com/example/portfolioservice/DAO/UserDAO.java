package com.example.portfolioservice.DAO;

import com.example.portfolioservice.models.*;
import com.google.common.base.Optional;
import org.immutables.mongo.repository.RepositorySetup;

import static com.example.portfolioservice.utils.Constants.FX_USD;


public class UserDAO
{

    private UserDBModelRepository repository;
    private UserDBModelRepository.Criteria where;



    public UserDAO()
    {
        repository =new UserDBModelRepository(RepositorySetup.forUri("mongodb://localhost:27017/portfolio"));
        where = repository.criteria();
    }


    public String createUser(User2 user)
    {
        UserDBModel userDB;
        userDB = ImmutableUserDBModel.builder()
                .userId(user.userId().get())
                .baseCurr(user.baseCurr().get())
                .currBal(user.currBal())
                .all_funds(user.all_funds().get()).build();

        repository.insert(userDB);
        return "User Created";

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
                    .userId(updated_user.userId()).currBal(user.currBal())
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
            balance.setCurrBal(user.get().currBal());
            balance.setBaseCurr(user.get().baseCurr());
            return balance;
        }
        return null;
    }

    public float updateBalance(String userId, float balance)
    {
        Optional<UserDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            repository.upsert(
                    ImmutableUserDBModel.builder()
                            .userId(user.get().userId())
                            .all_funds(user.get().all_funds())
                            .currBal(balance)
                            .build()
            );
            return user.get().currBal();
        }
        return 0;
    }
    public String updateBaseCurrency(String userId, String newCurrency)
    {
        Optional<UserDBModel> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            float newBalance = user.get().currBal() * (FX_USD.get(newCurrency)/FX_USD.get(user.get().baseCurr()));
            repository.upsert(
                    ImmutableUserDBModel.builder()
                            .userId(user.get().userId())
                            .all_funds(user.get().all_funds())
                            .currBal(newBalance)
                            .baseCurr(newCurrency)
                            .build()
            );
        }
        return user.get().baseCurr();
    }
}
