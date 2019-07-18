package io.tradingservice.tradingservice.repositories;


import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import io.tradingservice.tradingservice.models.*;
import io.tradingservice.tradingservice.utils.Constants;
import org.immutables.mongo.concurrent.FluentFuture;
import org.immutables.mongo.repository.RepositorySetup;

import java.util.ArrayList;
import java.util.List;

public class TransactionAccessObject {

    private UserTransactionRepository transactionRepository;

    public TransactionAccessObject() {
        transactionRepository = new UserTransactionRepository(RepositorySetup.forUri(Constants.mongoPort));
    }

    public boolean addTransaction(String userId, Transaction newTransaction) {
        if (transactionRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()) {
            FluentFuture<Optional<UserTransaction>> addition = transactionRepository.findByUserId(userId)
                                        .andModifyFirst()
                                        .addTransactions(newTransaction)
                                        .upsert();

        }
        else {
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(newTransaction);
            UserTransaction newUser = ImmutableUserTransaction.builder()
                                                .userId(userId)
                                                .transactions(transactions)
                                                .build();
            transactionRepository.insert(newUser); 
        }
        return true;
    }

    public List<Transaction> getAllTransactions(String userId) {
        List<Transaction> transactions = new ArrayList<>();
        if (transactionRepository.findByUserId(userId).fetchFirst().getUnchecked().isPresent()) {
            UserTransaction user = transactionRepository.findByUserId(userId).fetchFirst().getUnchecked().get();
            for (Transaction transaction: user.transactions()) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
