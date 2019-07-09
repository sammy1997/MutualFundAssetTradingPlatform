package com.mutualfundtrading.fundhandling.unittests;


import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.*;
import com.mutualfundtrading.fundhandling.services.EntitlementService;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EntitlementServiceUnitTests {
    @Mock
    private EntitlementDAO dao;

    @Mock
    private FundDAO fundDAO;

    @Mock
    private FundService fundService;

    @Mock
    private WebClient webClient;

    @InjectMocks
    EntitlementService service;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW1teTE5OTciLCJhdXRob3JpdGllcyI6WyJST0" +
            "xFX0FETUlOIiwiUk9MRV9UUkFERVIiXSwiaWF0IjoxNTYyNTY5MjcwLCJuYW1lIjoiU29tYnVkZGhhIiwiZXhwI" +
            "joxNTYyNTcyODcwfQ.slxEU0r6eoD76R4DSD-9wN7mTWwSACufKd8IyZWtTb-O50kfirugzBEjZ9AOiMyXzaZYx7" +
            "tf_kQR5yFAF27mXw";

    private Fund fundDb;
    private EntitlementParser entitlement;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp(){
        fundDb = ImmutableFund.builder().fundName("Hedge").fundNumber("1234")
                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
                .setCycle(2).invManager("GS").moodysRating(12).build();
        List<String> entitledTo  = new ArrayList<>();
        entitledTo.add("1234");
        entitlement = ImmutableEntitlementParser.builder().userId("sammy1997")
                .entitledTo(entitledTo).build();
    }

    @Test
    public void getEntitlementsServiceTest(){
        List<ImmutableFund> funds = new ArrayList<>();
        funds.add((ImmutableFund) fundDb);
        Mockito.when(dao.getEntitledFunds(Mockito.anyString())).thenReturn(funds);
        assertThat(funds).isEqualTo(service.getEntitlements(Mockito.anyString()));
    }

    @Test
    public void searchEntitlementsServiceTest(){
        List<Fund> fundName = new ArrayList<>();
        fundName.add(fundDb);

        List<Fund> fundNumber = new ArrayList<>();
        fundNumber.add(fundDb);

        List<Fund> invCurrency = new ArrayList<>();
        invCurrency.add(fundDb);

        List<Fund> invManager = new ArrayList<>();
        invManager.add(fundDb);

        List<String> entitledTo  = new ArrayList<>();
        entitledTo.add("1234");

        Mockito.when(dao.getEntitlementListForUser(Mockito.anyString())).thenReturn(entitledTo);
        String searchTerm = "12";
        Mockito.when(fundDAO.searchFundNameInEntitlements(searchTerm, entitledTo))
                .thenReturn(fundName);
        Mockito.when(fundDAO.searchFundNumberInEntitlements(searchTerm, entitledTo))
                .thenReturn(fundNumber);
        Mockito.when(fundDAO.searchInvCurrencyInEntitlements(searchTerm, entitledTo))
                .thenReturn(invCurrency);
        Mockito.when(fundDAO.searchInvManagerInEntitlements(searchTerm, entitledTo))
                .thenReturn(invManager);

        assertThat(fundName).isEqualTo(service.searchEntitlements(Mockito.anyString(),
                "Name", searchTerm));
        assertThat(fundNumber).isEqualTo(service.searchEntitlements(Mockito.anyString(),
                "Fund Number", searchTerm));
        assertThat(invCurrency).isEqualTo(service.searchEntitlements(Mockito.anyString(),
                "Currency", searchTerm));
        assertThat(invManager).isEqualTo(service.searchEntitlements(Mockito.anyString(),
                "Manager", searchTerm));
    }

    @Test
    public void deleteEntitlementsServiceTest(){
        // Test when user ID is missing
        Response response = service.deleteEntitlements(ImmutableEntitlementParser
                .builder().userId(Optional.empty()).entitledTo(Optional.empty()).build());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getEntity()).isEqualTo("User ID is missing");

        // Test when entitlement list is empty
        response = service.deleteEntitlements(ImmutableEntitlementParser
                .builder().userId("sammy1997").entitledTo(Optional.empty()).build());
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity()).isEqualTo("Fund list cannot be empty");

        // Test when user not found
        Mockito.when(dao.delete(entitlement.userId().get(), entitlement.entitledTo().get()))
                .thenReturn(null);

        response = service.deleteEntitlements(entitlement);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity()).isEqualTo("User with user ID " +
                entitlement.userId().get() + " not found.");

        // Test when user is found
        Mockito.when(dao.delete(entitlement.userId().get(), entitlement.entitledTo().get()))
                .thenReturn("Entitlements deleted");

        response = service.deleteEntitlements(entitlement);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("The required entitlements for user "+
                entitlement.userId().get() +" have been deleted.");
    }

//    @Test
//    public void addEntitlementsServiceTest(){
//        Mono<String> bodyMono = Mockito.mock(Mono.class);
//        Mockito.when(bodyMono.block()).thenReturn("[]");
//
//        Response response = service.addEntitlements(ImmutableEntitlementParser
//                .builder().userId(Optional.empty()).entitledTo(Optional.empty()).build(), token);
//        assertThat(response.getStatus()).isEqualTo(400);
//        assertThat(response.getEntity()).isEqualTo("User ID is missing");
//    }
}