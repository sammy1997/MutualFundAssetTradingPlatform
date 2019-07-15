package io.tradingservice.tradingservice.serviceTests;

import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.ImmutableTransaction;
import io.tradingservice.tradingservice.models.Transaction;
import io.tradingservice.tradingservice.repositories.TransactionAccessObject;
import io.tradingservice.tradingservice.services.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class TransactionServiceTest {

    @Mock
    TransactionAccessObject transactionAccessObject;

    @InjectMocks
    TransactionService transactionService;

    ImmutableTrade trade;
    ImmutableTransaction transaction;

    @Test
    public void successTest() {

        assert 1 == 1;
    }

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp() {

        trade = ImmutableTrade
                .builder()
                .fundNumber("1234")
                .fundName("Hedge")
                .avgNav((float) 22)
                .status("purchase")
                .quantity((float) 7)
                .invManager("GS")
                .setCycle("T+2")
                .invCurr("INR")
                .sAndPRating((float) 23.2)
                .moodysRating((float) 12)
                .build();
    }

    //Test for history of transactions
    @Test
    public void getAllTransactionsByIdServiceTest() {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Mockito.when(transactionAccessObject.getAllTransactions(Mockito.any(String.class)))
                .thenReturn(transactions);

        Assert.assertEquals(transactions, transactionService.getAllTransactionsById("1234"));
    }

    //Test for adding transactions
    @Test
    public void addTransactionByIdServiceTest() {

        transaction = ImmutableTransaction
                .builder()
                .fundNumber(trade.fundNumber())
                .fundName(trade.fundName())
                .avgNav(trade.avgNav())
                .status(trade.status())
                .quantity(trade.quantity())
                .invManager(trade.invManager())
                .setCycle(trade.setCycle())
                .invCurr(trade.invCurr())
                .sAndPRating(trade.sAndPRating())
                .moodysRating(trade.moodysRating())
                .time(new Timestamp(System.currentTimeMillis()))
                .build();

        Mockito.when(transactionAccessObject
                .addTransaction(Mockito.any(String.class), Mockito.any()))
                .thenReturn(true);

        Assert.assertEquals(true, transactionService.addTransactionById("123", trade));
    }
}