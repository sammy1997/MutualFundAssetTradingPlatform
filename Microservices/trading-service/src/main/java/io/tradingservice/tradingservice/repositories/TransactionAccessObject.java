package io.tradingservice.tradingservice.repositories;

import com.google.common.base.Optional;
import io.tradingservice.tradingservice.models.Transaction;
import io.tradingservice.tradingservice.models.UserTransaction;
import io.tradingservice.tradingservice.models.UserTransactionRepository;
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
        if (transactionRepository.find(userId).fetchFirst().getUnchecked().isPresent()) {
            FluentFuture<Optional<UserTransaction>> addition = transactionRepository.find(userId)
                                        .andModifyFirst()
                                        .addTransactions(newTransaction)
                                        .upsert();
            return true;
        }
        else return false;
    }

    public List<Transaction> getAllTransactions(String userId) {
        List<Transaction> transactions = new ArrayList<>();
        if (transactionRepository.find(userId).fetchFirst().getUnchecked().isPresent()) {
            UserTransaction user = transactionRepository.find(userId).fetchFirst().getUnchecked().get();
            for (Transaction transaction: user.transactions()) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}