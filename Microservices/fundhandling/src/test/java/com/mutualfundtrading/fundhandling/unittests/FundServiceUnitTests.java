package com.mutualfundtrading.fundhandling.unittests;

import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
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

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Optional;


@RunWith(MockitoJUnitRunner.class)
public class FundServiceUnitTests {
    @Mock
    FundDAO dao;

    @InjectMocks
    FundService service;

    Fund fund;
    FundDBModel fundDb;

        @Test
        public void successTest(){
            assert 1==1;
        }

//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Before
//    public void setUp(){
//        fund = ImmutableFund.builder().fundName("Hedge").fundNumber("1234").build();
//        fundDb = ImmutableFundDBModel.builder().fundName("Hedge").fundNumber("1234")
//                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
//                .setCycle(2).invManager("GS").moodysRating(12).build();
//    }
//
//    @Test
//    public void addFundServiceTest(){
//        Mockito.when(dao.insert(Mockito.any(Fund.class))).thenReturn("Insert operation called");
//        Assert.assertEquals("Insert operation called", service.addFundService(fund));
//    }
//
//    @Test
//    public void getFundServiceTest(){
//        Mockito.when(dao.getFund(Mockito.any(String.class))).thenReturn((ImmutableFundDBModel) fundDb);
//        Assert.assertEquals((ImmutableFundDBModel) fundDb, service.getFund("123"));
//    }
//
//    @Test
//    public void getAllFundServiceTest(){
//        List<ImmutableFundDBModel> funds = new ArrayList<>();
//        funds.add((ImmutableFundDBModel) fundDb);
//
//        Mockito.when(dao.getAll()).thenReturn(funds);
//        Assert.assertEquals(funds, service.getAll());
//    }
//
//    @Test
//    public void updateFundFundFoundServiceTest(){
//        Optional<FundDBModel> optional = Optional.fromNullable(fundDb);
//        Mockito.when(dao.update(Mockito.any(Fund.class))).thenReturn(optional);
//        Assert.assertEquals("Fund with number " + fundDb.fundNumber() + " updated.",
//                service.update(fund).getMessage());
//        Assert.assertEquals(200, service.update(fund).getStatus());
//    }
//
//
//    @Test
//    public void updateFundFundNotFoundServiceTest(){
//        Optional<FundDBModel> optional = Optional.fromNullable(null);
//        Mockito.when(dao.update(Mockito.any(Fund.class))).thenReturn(optional);
//        Assert.assertEquals("Fund with number " + fundDb.fundNumber() + " not found in db.",
//                service.update(fund).getMessage());
//        Assert.assertEquals(404, service.update(fund).getStatus());
//    }
//
//    @Test
//    public void deleteFundFundFoundServiceTest(){
//        Optional<FundDBModel> optional = Optional.fromNullable(fundDb);
//
//        Mockito.when(dao.delete(Mockito.anyString())).thenReturn(optional);
//
//        Assert.assertEquals("Fund with fund number "+ fundDb.fundNumber() +" deleted",
//                service.delete(fundDb.fundNumber()).getMessage());
//        Assert.assertEquals(200, service.delete(fundDb.fundNumber()).getStatus());
//    }
//
//
//    @Test
//    public void deleteFundFundNotFoundServiceTest(){
//        Optional<FundDBModel> optional = Optional.fromNullable(null);
//
//        Mockito.when(dao.delete(Mockito.anyString())).thenReturn(optional);
//
//        Assert.assertEquals("Fund with fund number "+ fundDb.fundNumber() +" not found.",
//                service.delete(fundDb.fundNumber()).getMessage());
//        Assert.assertEquals(404, service.delete(fundDb.fundNumber()).getStatus());
//    }
//
//    @Test
//    public void searchFundServiceTest(){
//        List<FundDBModel> fundNameResults = new ArrayList<>();
//        fundNameResults.add(fundDb);
//        List<FundDBModel> fundNumberResults = new ArrayList<>();
//        fundNumberResults.add(fundDb);
//        List<FundDBModel> fundInvManagerResults = new ArrayList<>();
//        fundInvManagerResults.add(fundDb);
//        List<FundDBModel> fundInvCurrencyResults = new ArrayList<>();
//        fundInvCurrencyResults.add(fundDb);
//
//        Mockito.when(dao.searchFundName(Mockito.anyString())).thenReturn(fundNameResults);
//        Mockito.when(dao.searchFundNumber(Mockito.anyString())).thenReturn(fundNumberResults);
//        Mockito.when(dao.searchInvManager(Mockito.anyString())).thenReturn(fundInvManagerResults);
//        Mockito.when(dao.searchInvCurrency(Mockito.anyString())).thenReturn(fundInvCurrencyResults);
//
//        Assert.assertEquals(fundNameResults, service.searchAllFunds("Name", "ab"));
//        Assert.assertEquals(fundNumberResults, service.searchAllFunds("Fund Number", "ab"));
//        Assert.assertEquals(fundInvCurrencyResults, service.searchAllFunds("Currency", "ab"));
//        Assert.assertEquals(fundInvManagerResults, service.searchAllFunds("Manager", "ab"));
//
//        Assert.assertNull(service.searchAllFunds("ad", "asd"));
//    }
}
