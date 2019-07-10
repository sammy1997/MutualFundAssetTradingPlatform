package com.mutualfundtrading.fundhandling.unittests;

import com.mutualfundtrading.fundhandling.controllers.EntitlementController;
import com.mutualfundtrading.fundhandling.controllers.EntitlementControllerModel;
import com.mutualfundtrading.fundhandling.models.*;
import com.mutualfundtrading.fundhandling.services.EntitlementServiceModel;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class EntitlementControllerUnitTests {

    @Mock
    private ServiceUtils serviceUtils;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYW1teTE5OTciLCJhdXRob3JpdGllcyI6WyJST0" +
            "xFX0FETUlOIiwiUk9MRV9UUkFERVIiXSwiaWF0IjoxNTYyNTY5MjcwLCJuYW1lIjoiU29tYnVkZGhhIiwiZXhwI" +
            "joxNTYyNTcyODcwfQ.slxEU0r6eoD76R4DSD-9wN7mTWwSACufKd8IyZWtTb-O50kfirugzBEjZ9AOiMyXzaZYx7" +
            "tf_kQR5yFAF27mXw";

    private Fund fundDb;
    private EntitlementParser entitlement;

    @Mock
    private EntitlementServiceModel service;

    @InjectMocks
    private EntitlementControllerModel controller = new EntitlementController();

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
    public void addEntitlementTest() {
        Mockito.when(service.addEntitlements(Mockito.any(EntitlementParser.class), Mockito.anyString()))
                .thenReturn(Response.status(200).entity("Add entitlements called").build());
        Response response = controller.createEntitlement(Mockito.anyString(), Mockito.any(EntitlementParser.class));
        System.out.println(response);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("Add entitlements called");
    }

    @Test
    public void updateEntitlementsTest() throws Exception{
        Mockito.when(service.updateEntitlements(Mockito.any(EntitlementParser.class)))
                .thenReturn(Response.status(200).entity("Update entitlements called").build());
        Response response = controller.updateEntitlements(Mockito.any(EntitlementParser.class));
        assertThat(response.getStatus()).isEqualTo("Update entitlements called");
    }

    @Test
    public void deleteEntitlementTest() throws Exception{
        Mockito.when(service.deleteEntitlements(Mockito.any(EntitlementParser.class)))
                .thenReturn(Response.status(200).entity("Delete entitlements called").build());
        assertThat(controller.deleteEntitlements(Mockito.any(EntitlementParser.class), Mockito.anyString()))
                .isEqualTo("Delete entitlements called");
    }

//    @Test
//    public void getEntitlementTest() throws Exception{
//        URI uri = new URI(baseUrl + "/get");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + token);
//
//        List<Fund> entitlements = new ArrayList<>();
//        entitlements.add(fundDb);
//
//        Mockito.when(service.getEntitlements(Mockito.anyString()))
//                .thenReturn(entitlements);
//
//        HttpEntity<EntitlementParser> request = new HttpEntity<>(entitlement, headers);
//        ResponseEntity<String> entity= this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//
//        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\"" +
//                ",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2" +
//                ",\"moodysRating\":12.0}]";
//        assertThat(entity.getBody()).isEqualTo(expected);
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    public void searchEntitlementsTest() throws Exception{
//        URI uri = new URI(baseUrl + "/search?field=any&term=any");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + token);
//
//        List<Fund> entitlements = new ArrayList<>();
//        entitlements.add(fundDb);
//
//        Mockito.when(service.searchEntitlements(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(entitlements);
//
//        HttpEntity<EntitlementParser> request = new HttpEntity<>(null, headers);
//        ResponseEntity<String> entity= this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//
//        String expected = "[{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\"" +
//                ",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2" +
//                ",\"moodysRating\":12.0}]";
//        assertThat(entity.getBody()).isEqualTo(expected);
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    public void getEntitledFundTest() throws Exception{
//        URI uri = new URI(baseUrl + "/get/fund?fundNumber=123");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + token);
//
//        List<Fund> entitlements = new ArrayList<>();
//        entitlements.add(fundDb);
//
//        //Test for fund found
//        Mockito.when(service.searchEntitlements(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(entitlements);
//
//        HttpEntity<EntitlementParser> request = new HttpEntity<>(null, headers);
//        ResponseEntity<String> entity= this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//
//        String expected = "{\"fundNumber\":\"1234\",\"fundName\":\"Hedge\",\"invManager\":\"GS\"" +
//                ",\"setCycle\":2,\"nav\":22.0,\"invCurrency\":\"INR\",\"sAndPRating\":23.2" +
//                ",\"moodysRating\":12.0}";
//        assertThat(entity.getBody()).isEqualTo(expected);
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // Test for fund not found
//        Mockito.when(service.searchEntitlements(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(null);
//
//        request = new HttpEntity<>(null, headers);
//        entity= this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//
//        expected = "{\"fundNumber\":\"\",\"fundName\":\"\",\"invManager\":\"\"" +
//                ",\"setCycle\":0,\"nav\":0.0,\"invCurrency\":\"\",\"sAndPRating\":0.0" +
//                ",\"moodysRating\":0.0}";
//        assertThat(entity.getBody()).isEqualTo(expected);
//        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    public void addEntitlementsFromFileTest() throws Exception {
//        URI uri = new URI(baseUrl + "/addEntitlements");
//
//        // Test file format check
//        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
//        ClassPathResource resource = new ClassPathResource("test.doc");
//        parameters.add("file", resource);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(404);
//        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
//        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
//
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        Assertions.assertThat(response.getBody()).isEqualTo("Provide excel or csv files");
//
//        resource = new ClassPathResource("test.xlsx");
//        parameters.remove("file");
//        parameters.add("file", resource);
//
//        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(200);
//        Mockito.when(serviceUtils.addEntitlementsFromCSV(Mockito.any()))
//                .thenReturn(Response.status(200).entity("File upload successful").build());
//
//        entity = new HttpEntity<>(parameters, headers);
//        response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(response.getBody()).isEqualTo("File upload successful");
//
//        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(400);
//
//        response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
//        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(400);
//        Assertions.assertThat(response.getBody()).isEqualTo("Error while uploading file. Try again");
//    }
}
