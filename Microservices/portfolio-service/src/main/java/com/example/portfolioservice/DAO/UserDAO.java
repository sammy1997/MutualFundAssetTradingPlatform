package com.example.portfolioservice.DAO;

import com.example.portfolioservice.models.*;
import com.google.common.base.Optional;
import org.immutables.mongo.repository.RepositorySetup;


@SuppressWarnings("ALL")
public class UserDAO {

    private UserRepository repository;
    private UserRepository.Criteria where;


    public UserDAO() {
        repository = new UserRepository(RepositorySetup.forUri("mongodb://localhost:27017/portfolio"));
        where = repository.criteria();
    }


    public String createUser(final UserParser userParser) {
        User user;
        user = ImmutableUser.builder()
                .userId(userParser.userId().get())
                .baseCurr(userParser.baseCurr().get())
                .currBal(userParser.currBal())
                .all_funds(userParser.all_funds().get()).build();

        repository.insert(user);
        return "UserParser Created";

    }

    public Optional<User> getUser(final String id) {
        return repository.find(where.userId(id)).fetchFirst().getUnchecked();
    }

    public Optional<User> update(UserParser userParser) {
        Optional<User> user = repository.find(where.userId(userParser.userId().get()))
                              .fetchFirst().getUnchecked();
        if(user.isPresent()) {
            User updated_user = user.get();
            repository.upsert(
                    ImmutableUser.builder()
                    .userId(updated_user.userId()).currBal(userParser.currBal())
                            .baseCurr(userParser.baseCurr().isPresent() ? userParser.baseCurr().get()
                                      : updated_user.baseCurr())
                            .all_funds(userParser.all_funds().isPresent() ? userParser.all_funds().get()
                                      : updated_user.all_funds())
                    .build()
            );
        }
        return user;
    }

    public Optional<User> delete(final String userId) {
        return repository.findByUserId(userId).deleteFirst().getUnchecked();
    }

    public Optional<BalanceInfo> getBalance(String userId) {
        Optional<User> user = repository.find(where.userId(userId))
                              .fetchFirst().getUnchecked();
        if(user.isPresent()) {
            BalanceInfo balance = new BalanceInfo();
            balance.setCurrBal(user.get().currBal());
            balance.setBaseCurr(user.get().baseCurr());
            return Optional.of(balance);
        }
        return Optional.absent();
    }

    public float updateBalance(final String userId, float balance) {
        Optional<User> user = repository.find(where.userId(userId))
                              .fetchFirst().getUnchecked();
        if(user.isPresent()) {
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
    public String updateBaseCurrency(final String userId, float originalValue,
                                     float newValue, String newCurrency) {
        Optional<User> user = repository.find(where.userId(userId))
                .fetchFirst().getUnchecked();
        if(user.isPresent()) {
            float newBalance = user.get().currBal()
                    * (newValue / originalValue);
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
