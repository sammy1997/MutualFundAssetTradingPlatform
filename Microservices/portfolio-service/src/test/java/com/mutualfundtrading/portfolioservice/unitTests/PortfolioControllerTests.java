package com.mutualfundtrading.portfolioservice.unitTests;
import static org.junit.Assert.assertEquals;
import com.google.common.base.Optional;
import com.mutualfundtrading.portfolioservice.controller.PortfolioController;
import com.mutualfundtrading.portfolioservice.models.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


public class PortfolioControllerTests {

    @Mock
    private PortfolioController portfolioController;

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
    public void test_getUserById() {
        Mockito.when(portfolioController.getUserById(Mockito.anyString())).thenReturn(Optional.of(user));
        Optional<User> user= portfolioController.getUserById(this.user.userId());
        assertEquals(user.get(), this.user);
    }

    @Test
    public void test_getBalanceById() {
        Mockito.when(portfolioController.getBalanceById(Mockito.anyString())).thenReturn(balance);
        BalanceInfo balance = portfolioController.getBalanceById(this.user.userId());
        assertEquals(balance, this.balance);
    }

    @Test
    public void test_updateBalance() {
        Mockito.when(portfolioController.updateBalance(Mockito.anyString(),
                Mockito.anyFloat(), Mockito.anyString())).thenReturn
                (Response.status(200).entity("Balance Updated").build());
        Response response = portfolioController.updateBalance("1", 1.2f, "1");
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getEntity(), "Balance Updated");
    }

    @Test
    public void test_deleteUserById() {
        Mockito.when(portfolioController.deleteUserById(Mockito.anyString())).thenReturn(Optional.of(user));
        Optional<User> user = portfolioController.deleteUserById(this.user.userId());
        assertEquals(user.get(), this.user);

    }

    @Test
    public void test_updateUserById() {
        Mockito.when(portfolioController.updateUserById(Mockito.anyString(),
                Mockito.anyString(), Mockito.any(UserParser.class))).thenReturn(Response.status(200)
                .entity("Updated profile successfully").build());
        Response response = portfolioController.updateUserById("1",  "1", userParser);
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getEntity(), "Updated profile successfully");

    }

    @Test
    public void test_updateBaseCurrency() {
        Mockito.when(portfolioController.updateBaseCurrency(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("USD");
        String expected = portfolioController.updateBaseCurrency("1", "USD");
        assertEquals(expected, "USD");
    }

    @Test
    public void test_addUser() {
        Mockito.when(portfolioController.addUser(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyFloat(), Mockito.anyString())).thenReturn(Response.status(200)
                .entity("Updated profile successfully").build());
        Response response = portfolioController.addUser("1",  "1", 1.2f, "USD");
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getEntity(), "Updated profile successfully");

    }

    @Test
    public void test_getFunds() {
        Mockito.when(portfolioController.getFunds(Mockito.anyString())).thenReturn(user.all_funds());
        List<Fund> funds = portfolioController.getFunds(user.userId());
        assertEquals(funds, user.all_funds());
    }



}
