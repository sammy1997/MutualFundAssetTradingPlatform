package io.tradingservice.tradingservice.serviceTests;

import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.TradeParser;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import io.tradingservice.tradingservice.services.UserTradeService;
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

@RunWith(MockitoJUnitRunner.class)

public class ServiceTests {

    @Mock
    UserAccessObject userAccessObject;

    @InjectMocks
    UserTradeService userTradeService;

    ImmutableTrade trade;
    List<ImmutableTrade> trades;
    TradeParser t;

    @Test
    public void successTest(){
        assert 1==1;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setUp(){
        //trade = ImmutableTrade.builder().fundName("Hedge").fundNumber("1234").build();
        trade = ImmutableTrade.builder().fundNumber("1234").fundName("Hedge").avgNav((float)22)
                .status("purchase").quantity((float)7).invManager("GS").setCycle(2).invCurr("INR")
                .sAndPRating((float)23.2).moodysRating((float)12).build();
    }

    @Test
    public void getAllTradesServiceTest(){
        List<ImmutableTrade> trades = new ArrayList<>();
        trades.add(trade);

        Mockito.when(userAccessObject.getAllTradesByUserId(Mockito.any(String.class))).thenReturn(trades);
        Assert.assertEquals(trades, userTradeService.getAllTrades("1234"));
    }

    @Test
    public void addUserByIdServiceTest(){
        Mockito.when(userAccessObject.addUser(Mockito.any(String.class))).thenReturn(true);
        Assert.assertEquals(true, userTradeService.addUserById("123"));
    }

//    @Test
//    public void makeTradesServiceTest() {
//        List<ImmutableTrade> trades = new ArrayList<>();
//
//        trade = ImmutableTrade.builder().fundNumber("1").fundName("Hedge").avgNav((float) 17)
//                .status("purchase").quantity((float) 9).invManager("GS").setCycle(4).invCurr("INR")
//                .sAndPRating((float) 2.32).moodysRating((float) 13).build();
//
//        trades.add(trade);
//
//        Mockito.when(userAccessObject.addTrade(Mockito.anyString(), Mockito.any() , Mockito.anyFloat(), Mockito.anyString()))
//                .thenReturn();
//        Assert.assertEquals(200, trades.add(trade));
//    }

    /*
    @Test
    public void verifyTradesServiceTest(){

    }

    @Test
    public void exchangeTradeServiceTest(){
        ImmutableTrade.Builder t = new TradeParser();

        t = ImmutableTrade.builder().fundNumber("1").quantity((float)9).status("purchase");

        trades.add((trade);

        *//*Mockito.when(userAccessObject.addTrade(Mockito.anyList(Trade))).thenReturn(trades);
        Assert.assertEquals(200, trades.add(trade).Response.status());*//*
    }

    @Test
    public void updateUserTradeServiceTest(){
        Optional<trades> optional = Optional.fromNullable(null);
        Mockito.when(userTradeService.updateUser(Mockito.any(Trade.class))).thenReturn(optional);
        *//*Assert.assertEquals("Fund with number " + fundDb.fundNumber() + " not found in db.",
                service.update(fund).getMessage());*//*
        Assert.assertEquals(404, userTradeService.updateUser().getStatus());
    } */
}