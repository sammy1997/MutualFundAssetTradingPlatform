package com.mutualfundtrading.portfolioservice.unitTests;

import com.google.common.base.Optional;
import com.mutualfundtrading.portfolioservice.DAO.UserDAO;
import com.mutualfundtrading.portfolioservice.models.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

public class UserDAOUnitTests {

    @Mock
    private UserDAO dao;

    private User user;
    private UserParser userParser;
    private BalanceInfo balance = new BalanceInfo();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initialize(){
        List<Fund> funds =  new ArrayList<>();

        user = ImmutableUser.builder()
                .userId("1")
                .currBal(34)
                .all_funds(funds)
                .baseCurr("INR").build();
        userParser = ImmutableUserParser.builder()
                .userId("1")
                .currBal(34)
                .all_funds(funds)
                .baseCurr("INR").build();
        this.balance.setCurrBal((float)34);
        this.balance.setBaseCurr("INR");
    }

    @Test
    public void test_createUser() {
        Mockito.when(dao.createUser(userParser)).thenReturn("User Created");
        String expected = dao.createUser(userParser);
        assertEquals(expected, "User Created");
    }

    @Test
    public void test_getUser() {
        Mockito.when(dao.getUser(Mockito.any())).thenReturn(Optional.of(user));
        Optional<User> user = dao.getUser(this.user.userId());
        assertEquals(user.get(), this.user);
    }

    @Test
    public void test_update() {
        Mockito.when(dao.update(userParser)).thenReturn(Optional.of(user));
        Optional<User> user = dao.update(userParser);
        assertEquals(user.get(), this.user);
    }

    @Test
    public void test_delete() {
        Mockito.when(dao.delete(Mockito.anyString())).thenReturn(Optional.of(user));
        Optional<User> user = dao.delete(this.user.userId());
        assertEquals(user.get(), this.user);
    }

    @Test
    public void test_getBalance() {
        Mockito.when(dao.getBalance(Mockito.anyString())).thenReturn(Optional.of(this.balance));
        Optional<BalanceInfo> balance = dao.getBalance(user.userId());
        assertEquals(balance.get(), this.balance);
    }

    @Test
    public void test_updateBalance() {
        Mockito.when(dao.updateBalance(Mockito.anyString(), Mockito.anyFloat())).thenReturn(user.currBal());
        float balance = dao.updateBalance(user.userId(), user.currBal());
        assert(balance == user.currBal());
    }

    @Test
    public void test_updateBaseCurrency() {
        Mockito.when(dao.updateBaseCurrency(Mockito.anyString(), Mockito.anyString())).thenReturn("USD");
        String currency = dao.updateBaseCurrency(user.userId(), user.baseCurr());
        assertEquals(currency, "USD");
    }
}
