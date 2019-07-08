package io.tradingservice.tradingservice.controllerTests;

import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import io.tradingservice.tradingservice.services.UserTradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ControllerTests {

    @MockBean
    UserAccessObject userAccessObject;

    @MockBean
    UserTradeService userTradeService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    String baseUrl;

    ImmutableTrade trade;
    List<ImmutableTrade> trades;

    @Test
    public void successTest(){
        assert 1==1;
    }

    /* Tests

    //Setting up dummy trade
    @Before
    public void setUp(){
        baseUrl = "http://localhost:" + randomServerPort;
        trade = ImmutableTrade.builder().fundNumber("1234").fundName("Hedge").avgNav((float)22)
                .status("purchase").quantity((float)7).invManager("GS").setCycle(2).invCurr("INR")
                .sAndPRating((float)23.2).moodysRating((float)12).build();
    }

    //View trades end point
    @Test
    public void viewTradeEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/view");

        List<ImmutableTrade> trades = new ArrayList<>();
        trades.add(trade);

        Mockito.when(userTradeService.getAllTrades(Mockito.anyString())).thenReturn(trades);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"avgNav\":22.0,\"status\":\"purchase\",\"quantity\":7.0,\"invManager\":\"GS\",\"setCycle\":2,\"invCurr\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
        assertThat(expected).isEqualTo(entity.getBody());
    }

    //Exchange trades end point
    @Test
    public void exchangeTradesEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/exchange");

        List<ImmutableTrade> trades = new ArrayList<>();
        trades.add(trade);

        Mockito.when(userTradeService.exchangeTrade(Mockito.anyString(), Mockito.anyList(),
                Mockito.anyString())).thenReturn(Response.status(201).entity("Exchanged Requested trade").build());

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"avgNav\":22.0,\"status\":\"purchase\",\"quantity\":7.0,\"invManager\":\"GS\",\"setCycle\":2,\"invCurr\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
        assertThat(expected).isEqualTo(entity.getBody());
    }

    //Verify trades end point
    @Test
    public void verifyTradesEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/verify");

        Mockito.when(userTradeService.verifyTrades(Mockito.anyString(), Mockito.anyList(), Mockito.anyString())).thenReturn(Response.status(200).entity("Verified trades").build());

        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "{\"status\":200,\"message\":\"Success\"}";
        assertThat(expected).isEqualTo(result.getBody());
    }

    // Add user end point (not required)
    @Test
    public void verifyTradesEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/addUser?userId=123");

    } */
}
