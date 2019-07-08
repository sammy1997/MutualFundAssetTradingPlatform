package com.example.portfolioservice.DAO;

import com.example.portfolioservice.models.*;
import com.google.common.base.Optional;
import org.immutables.mongo.repository.RepositorySetup;

import static com.example.portfolioservice.utils.Constants.FX_USD;


public class UserDAO
{

    private UserRepository repository;
    private UserRepository.Criteria where;



    public UserDAO()
    {
        repository =new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/portfolio"));
        where = repository.criteria();
    }


    public String createUser(UserParser userParser)
    {
        User userDB;
        userDB = ImmutableUser.builder()
                .userId(userParser.userId().get())
                .baseCurr(userParser.baseCurr().get())
                .currBal(userParser.currBal())
                .all_funds(userParser.all_funds().get()).build();

        repository.insert(userDB);
        return "UserParser Created";

    }

    public ImmutableUser getUser(String id)
    {
        Optional<User> user =repository.find(where.userId(id)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            return (ImmutableUser) user.get();
        }
        return null;
    }

    public Optional<User> update(UserParser userParser)
    {
        Optional<User> userDB = repository.find(where.userId(userParser.userId().get())).fetchFirst().getUnchecked();
        if(userDB.isPresent())
        {
            User updated_user = userDB.get();
            repository.upsert(
                    ImmutableUser.builder()
                    .userId(updated_user.userId()).currBal(userParser.currBal())
                            .baseCurr(userParser.baseCurr().isPresent()? userParser.baseCurr().get(): updated_user.baseCurr())
                            .all_funds(userParser.all_funds().isPresent()? userParser.all_funds().get():updated_user.all_funds())
                    .build()
            );
        }
        return userDB;
    }

    public Optional<User> delete(String userId)
    {
        return repository.findByUserId(userId).deleteFirst().getUnchecked();
    }

    public BalanceInfo getBalance(String userId)
    {
        Optional<User> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
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
        Optional<User> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            repository.upsert(
                    ImmutableUser.builder()
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
        Optional<User> user = repository.find(where.userId(userId)).fetchFirst().getUnchecked();
        if(user.isPresent())
        {
            float newBalance = user.get().currBal() * (FX_USD.get(newCurrency)/FX_USD.get(user.get().baseCurr()));
            repository.upsert(
                    ImmutableUser.builder()
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
