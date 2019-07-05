package io.tradingservice.tradingservice.controllerTests;

import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.Trade;
import io.tradingservice.tradingservice.models.TradeParser;
import io.tradingservice.tradingservice.repositories.UserAccessObject;
import io.tradingservice.tradingservice.services.UserTradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ControllerTests {

    @MockBean
    UserAccessObject userAccessObject

    @MockBean
    UserTradeService userTradeService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    String baseUrl;

    TradeParser trade;
    List<ImmutableTrade> trades;

    @Test
    public void successTest(){
        assert 1==1;
    }

    @Before
    public void setUp(){
        baseUrl = "http://localhost:"+randomServerPort+"/api/";
        trade = ImmutableTrade.builder().fundNumber("1234").fundName("Hedge").avgNav((float)22)
                .status("purchase").quantity(float(7)).invManager("GS").setCycle(2).invCurrency("INR")
                .sAndPRating((float)23.2).moodysRating(12).build();
    }

    @Test
    public void viewTradeEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/view");
        Mockito.when(userTradeService.getAllTrades(Mockito.any(String.class))).thenReturn((List<ImmutableTrade>) trades);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//
//        HttpEntity<Fund> request = new HttpEntity<>(fund, headers);
//
//        ResponseEntity<String> entity= this.restTemplate.postForEntity(uri, request, String.class);
//
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        String expected = "{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}";
//        assertThat(expected).isEqualTo(entity.getBody());
//    }
//
//    @Test
//    public void getAllFundsEndpointTest() throws Exception{
//        URI uri = new URI(baseUrl + "/all");
//
//        List<ImmutableFundDBModel> funds = new ArrayList<>();
//        funds.add((ImmutableFundDBModel) fundDb);
//
//        Mockito.when(service.getAll()).thenReturn(funds);
//
//        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);
//
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
//        assertThat(expected).isEqualTo(entity.getBody());
//    }
//
//    @Test
//    public void searchFundsEndpointTest() throws Exception{
//        URI uri = new URI(baseUrl + "/search?field=Name&term=ab");
//
//        List<FundDBModel> funds = new ArrayList<>();
//        funds.add(fundDb);
//
//        Mockito.when(service.searchAllFunds(Mockito.anyString(), Mockito.anyString())).thenReturn(funds);
//
//        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);
//
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
//        assertThat(expected).isEqualTo(entity.getBody());
//    }
//
//    @Test
//    public void deleteFundsEndpointTest() throws Exception{
//        URI uri = new URI(baseUrl + "/delete?fundNumber=123");
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//
//        HttpEntity<String> request = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
//
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        String expected = "{\"status\":200,\"message\":\"Success\"}";
//        assertThat(expected).isEqualTo(result.getBody());
//    }
}
}
