package io.tradingservice.tradingservice.services;

import io.tradingservice.tradingservice.models.ImmutableTransaction;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.Transaction;
import io.tradingservice.tradingservice.repositories.TransactionAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionAccessObject transactionAccessObject;

    //function to add transactions by user ID
    public Response addTransactionById(String userId, Trade newTrade) {
        Transaction newTransaction = ImmutableTransaction.builder()
                .fundNumber(newTrade.fundNumber())
                .fundName(newTrade.fundName())
                .avgNav(newTrade.avgNav())
                .status(newTrade.status())
                .quantity(newTrade.quantity())
                .invManager(newTrade.invManager())
                .setCycle(newTrade.setCycle())
                .invCurr(newTrade.invCurr())
                .sAndPRating(newTrade.sAndPRating())
                .moodysRating(newTrade.moodysRating())
                .time(new Timestamp(System.currentTimeMillis()))
                .build();

        boolean success = transactionAccessObject.addTransaction(userId, newTransaction);
        if (success)
            return Response.status(Response.Status.OK).entity("Transaction added").build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad request").build();
    }

    //function to get history of transactions
    public Response getAllTransactionsById(String userId) {

        List<Transaction> transactions = transactionAccessObject.getAllTransactions(userId);
        if (transactions.isEmpty())
            return Response.status(Response.Status.OK).entity("No trades purchased/sold.").build();
        else
            return Response.status(Response.Status.OK).entity(transactions).build();
    }
}