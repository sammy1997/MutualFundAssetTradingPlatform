package com.mutualfundtrading.fundhandling.unittests;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.mutualfundtrading.fundhandling.controllers.FundController;
import com.mutualfundtrading.fundhandling.messages.Message;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundDBModel;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundDBModel;
import com.mutualfundtrading.fundhandling.services.FundService;
import com.mutualfundtrading.fundhandling.utils.TestUtils;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


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

    Fund fund;
    FundDBModel fundDb;

    @Before
    public void setUp(){
        baseUrl = "http://localhost:"+randomServerPort+"/api/funds";
        fund = ImmutableFund.builder().fundName("Hedge").fundNumber("1234").build();
        fundDb = ImmutableFundDBModel.builder().fundName("Hedge").fundNumber("1234")
                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
                .setCycle(2).invManager("GS").moodysRating(12).build();
    }

    @Test
    public void createFundEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/create");

        Mockito.when(service.addFundService(Mockito.any(Fund.class))).thenReturn("Create service called");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Fund> request = new HttpEntity<>(fund, headers);

        ResponseEntity<String> entity= this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Create service called");
    }

    @Test
    public void getFundEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/retrieve");
        Mockito.when(service.getFund(Mockito.any(String.class))).thenReturn((ImmutableFundDBModel) fundDb);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Fund> request = new HttpEntity<>(fund, headers);

        ResponseEntity<String> entity= this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String expected = "{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}";
        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void getAllFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/all");

        List<ImmutableFundDBModel> funds = new ArrayList<>();
        funds.add((ImmutableFundDBModel) fundDb);

        Mockito.when(service.getAll()).thenReturn(funds);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void searchFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/search?field=Name&term=ab");

        List<FundDBModel> funds = new ArrayList<>();
        funds.add(fundDb);

        Mockito.when(service.searchAllFunds(Mockito.anyString(), Mockito.anyString())).thenReturn(funds);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2,\"moodysRating\":12.0}]";
        assertThat(expected).isEqualTo(entity.getBody());
    }

    @Test
    public void deleteFundsEndpointTest() throws Exception{
        URI uri = new URI(baseUrl + "/delete?fundNumber=123");

        Mockito.when(service.delete(Mockito.anyString())).thenReturn(new Message(200, "Success"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "{\"status\":200,\"message\":\"Success\"}";
        assertThat(expected).isEqualTo(result.getBody());
    }
}
