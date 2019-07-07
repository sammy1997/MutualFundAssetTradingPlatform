package com.mutualfundtrading.fundhandling.unittests;

import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundParser;
import com.mutualfundtrading.fundhandling.services.FundService;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FundControllerUnitTests {

    @MockBean
    FundService service;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    String baseUrl;

    FundParser fund;
    Fund fundDb;

    @Test
    public void successTest(){
        assert 1==1;
    }

    @Before
    public void setUp(){
        baseUrl = "http://localhost:"+randomServerPort+"/api/funds";
        fund = ImmutableFundParser.builder().fundName("Hedge").fundNumber("1234").build();
        fundDb = ImmutableFund.builder().fundName("Hedge").fundNumber("1234")
                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
                .setCycle(2).invManager("GS").moodysRating(12).build();
    }

    @Test
    public void createFundEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/create");

        Mockito.when(service.addFundService(Mockito.any(FundParser.class)))
                .thenReturn(Response.status(200).entity("Fund created").build());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<FundParser> request = new HttpEntity<>(fund, headers);

        ResponseEntity<String> entity= this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Fund created");
    }

    @Test
    public void getFundEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/retrieve");
        Mockito.when(service.getFund(Mockito.any(String.class))).thenReturn((ImmutableFund) fundDb);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<FundParser> request = new HttpEntity<>(fund, headers);
        ResponseEntity<String> entity= this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\"," +
                "\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\"" +
                ",\"sAndPRating\":23.2,\"moodysRating\":12.0}";

        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void getAllFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/all");

        List<ImmutableFund> funds = new ArrayList<>();
        funds.add((ImmutableFund) fundDb);

        Mockito.when(service.getAll()).thenReturn(funds);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\"" +
                ",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2," +
                "\"moodysRating\":12.0}]";

        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void searchFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/search?field=Name&term=ab");

        List<Fund> funds = new ArrayList<>();
        funds.add(fundDb);

        Mockito.when(service.searchAllFunds(Mockito.anyString(), Mockito.anyString())).thenReturn(funds);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\"" +
                ",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2" +
                ",\"moodysRating\":12.0}]";

        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void updateFundEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/update");

        Mockito.when(service.update(Mockito.any(FundParser.class)))
                .thenReturn(Response.status(200).entity("Updated fund").build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> entity= this.restTemplate.exchange(uri, HttpMethod.PATCH, request, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Updated fund");
    }

    @Test
    public void deleteFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/delete?fundNumber=123");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Mockito.when(service.delete(Mockito.anyString()))
                .thenReturn(Response.status(200).entity("Deleted fund").build());

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Deleted fund");
    }
}
