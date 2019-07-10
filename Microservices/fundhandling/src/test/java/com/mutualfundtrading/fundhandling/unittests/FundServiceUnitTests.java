package com.mutualfundtrading.fundhandling.unittests;

import com.google.common.base.Optional;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundParser;
import com.mutualfundtrading.fundhandling.services.FundService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class FundServiceUnitTests {
    @Mock
    private FundDAO dao;

    @InjectMocks
    private FundService service;

    private FundParser fund;
    private Fund fundDb;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp(){
        fund = ImmutableFundParser.builder().fundName("Hedge").fundNumber("1234").build();
        fundDb = ImmutableFund.builder().fundName("Hedge").fundNumber("1234")
                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
                .setCycle(2).invManager("GS").moodysRating(12).build();
    }

    @Test
    public void addFundServiceTest(){
        Mockito.when(dao.insert(Mockito.any(FundParser.class))).thenReturn("Successfully added");
        Response response = service.addFundService(fund);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("Successfully added");

        Mockito.when(dao.insert(Mockito.any(FundParser.class))).thenReturn("Some fields are missing");
        response = service.addFundService(fund);
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getEntity()).isEqualTo("Some fields are missing");

        Mockito.when(dao.insert(Mockito.any(FundParser.class))).thenReturn("Fund with this fund number already exists");
        response = service.addFundService(fund);
        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getEntity()).isEqualTo("Fund with this fund number already exists");
    }

    @Test
    public void getFundServiceTest(){
        Mockito.when(dao.getFund(Mockito.any(String.class))).thenReturn((ImmutableFund) fundDb);
        Assert.assertEquals((ImmutableFund) fundDb, service.getFund("123"));
    }

    @Test
    public void getAllFundServiceTest(){
        List<ImmutableFund> funds = new ArrayList<>();
        funds.add((ImmutableFund) fundDb);

        Mockito.when(dao.getAll()).thenReturn(funds);
        Assert.assertEquals(funds, service.getAll());
    }

    @Test
    public void updateFundServiceTest(){
        // Test update when fund is present
        Optional<Fund> optional = Optional.fromNullable(fundDb);
        Mockito.when(dao.update(Mockito.any(FundParser.class))).thenReturn(optional);
        Response response = service.update(fund);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("Fund with number " + fund.fundNumber() + " updated.");

        // Test update when fund is absent
        optional = Optional.fromNullable(null);
        Mockito.when(dao.update(Mockito.any(FundParser.class))).thenReturn(optional);
        response = service.update(fund);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity()).isEqualTo("Fund with number " + fund.fundNumber() + " not found in db.");
    }

    @Test
    public void deleteFundFundFoundServiceTest(){
        // Test fund for presence
        Optional<Fund> optional = Optional.fromNullable(fundDb);
        Mockito.when(dao.delete(Mockito.anyString())).thenReturn(optional);
        Response response = service.delete(fundDb.fundNumber());
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat("Fund with fund number "+ fundDb.fundNumber() +" deleted")
                .isEqualTo(response.getEntity());

        // Test fund for absence
        optional = Optional.fromNullable(null);
        Mockito.when(dao.delete(Mockito.anyString())).thenReturn(optional);
        response = service.delete(fundDb.fundNumber());
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat("Fund with fund number "+ fundDb.fundNumber() +" not found.")
                .isEqualTo(response.getEntity());
    }

    @Test
    public void searchFundServiceTest(){
        List<Fund> fundNameResults = new ArrayList<>();
        fundNameResults.add(fundDb);

        List<Fund> fundNumberResults = new ArrayList<>();
        fundNumberResults.add(fundDb);

        List<Fund> fundInvManagerResults = new ArrayList<>();
        fundInvManagerResults.add(fundDb);

        List<Fund> fundInvCurrencyResults = new ArrayList<>();
        fundInvCurrencyResults.add(fundDb);

        Mockito.when(dao.searchFundName(Mockito.anyString())).thenReturn(fundNameResults);
        Mockito.when(dao.searchFundNumber(Mockito.anyString())).thenReturn(fundNumberResults);
        Mockito.when(dao.searchInvManager(Mockito.anyString())).thenReturn(fundInvManagerResults);
        Mockito.when(dao.searchInvCurrency(Mockito.anyString())).thenReturn(fundInvCurrencyResults);

        Assert.assertEquals(fundNameResults, service.searchAllFunds("Name", "ab"));
        Assert.assertEquals(fundNumberResults, service.searchAllFunds("Fund Number", "ab"));
        Assert.assertEquals(fundInvCurrencyResults, service.searchAllFunds("Currency", "ab"));
        Assert.assertEquals(fundInvManagerResults, service.searchAllFunds("Manager", "ab"));

        Assert.assertNull(service.searchAllFunds("ad", "asd"));
    }
}
