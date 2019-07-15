package com.mutualfundtrading.fundhandling.unittests;

import com.mutualfundtrading.fundhandling.controllers.FundController;
import com.mutualfundtrading.fundhandling.controllers.FundControllerModel;
import com.mutualfundtrading.fundhandling.models.Fund;
import com.mutualfundtrading.fundhandling.models.FundParser;
import com.mutualfundtrading.fundhandling.models.ImmutableFund;
import com.mutualfundtrading.fundhandling.models.ImmutableFundParser;
import com.mutualfundtrading.fundhandling.services.EntitlementServiceModel;
import com.mutualfundtrading.fundhandling.services.FundService;
import com.mutualfundtrading.fundhandling.services.FundServiceModel;
import com.mutualfundtrading.fundhandling.utils.ServiceUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FundControllerUnitTests {

    @Mock
    private FundServiceModel service;

    @Mock
    private EntitlementServiceModel entitlementService;

    @Mock
    private ServiceUtils serviceUtils;

    @InjectMocks
    private FundControllerModel fundController = new FundController();

    private FundParser fund;
    private Fund fundDb;


    @Before
    public void setUp(){
        fund = ImmutableFundParser.builder().fundName("Hedge").fundNumber("1234").build();
        fundDb = ImmutableFund.builder().fundName("Hedge").fundNumber("1234")
                .sAndPRating((float)23.2).nav(22).invCurrency("INR")
                .setCycle("2").invManager("GS").moodysRating(12).build();
    }

    @Test
    public void createFundEndpointTest() {
        Mockito.when(service.addFundService(Mockito.any(FundParser.class)))
                .thenReturn(Response.status(200).entity("Fund created").build());

        Response response = fundController.createFund(fund);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("Fund created");
    }

    @Test
    public void getFundEndpointTest() {
        Mockito.when(service.getFund(Mockito.any(String.class))).thenReturn(fundDb);
        Fund f = fundController.getFund(fund);
        assertThat(f).isEqualTo(fundDb);
    }

    @Test
    public void getAllFundsEndpointTest() {
        List<Fund> funds = new ArrayList<>();
        funds.add(fundDb);

        Mockito.when(service.getAll()).thenReturn(funds);
        List<Fund> results = fundController.getAll();
        assertThat(results).isEqualTo(funds);
    }

    @Test
    public void searchFundsEndpointTest() {
        List<Fund> funds = new ArrayList<>();
        funds.add(fundDb);
        Mockito.when(service.searchAllFunds(Mockito.anyString(), Mockito.anyString())).thenReturn(funds);
        assertThat(funds).isEqualTo(fundController.search(Mockito.anyString(), Mockito.anyString()));
    }

    @Test
    public void updateFundEndpointTest() {
        Mockito.when(service.update(Mockito.any(FundParser.class)))
                .thenReturn(Response.status(200).entity("Updated fund").build());
        assertThat(fundController.updateFund(fund).getEntity()).isEqualTo("Updated fund");
        assertThat(fundController.updateFund(fund).getStatus()).isEqualTo(200);
    }

    @Test
    public void deleteFundsEndpointTest() {
        Mockito.when(service.delete(Mockito.anyString()))
                .thenReturn(Response.status(200).entity("Deleted fund").build());
        Response result = fundController.delete(Mockito.anyString());
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getEntity()).isEqualTo("Deleted fund");
    }

    @Test
    public void addFundFromFileTest() {
        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(404);
        Response response = fundController.uploadCsvFile(Mockito.any(), Mockito.any());
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getEntity()).isEqualTo("Provide excel or csv files");

        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(400);
        response = fundController.uploadCsvFile(Mockito.any(), Mockito.any());
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getEntity()).isEqualTo("Error while uploading file. Try again");


        Mockito.when(serviceUtils.fileUpload(Mockito.any(), Mockito.any())).thenReturn(200);
        Mockito.when(serviceUtils.addFundsFromCSV(Mockito.any()))
                .thenReturn(Response.status(200).entity("File upload successful").build());
        response = fundController.uploadCsvFile(Mockito.any(), Mockito.any());
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo("File upload successful");
    }

    @Test
    public void getEntitlementsForUserTest() throws Exception {
        List<Fund> funds = new ArrayList<>();
        funds.add(fundDb);

        Mockito.when(entitlementService.getEntitlements(Mockito.anyString()))
                .thenReturn(funds);
        assertThat(fundController.getUserEntitlements(Mockito.anyString())).isEqualTo(funds);
    }
}
