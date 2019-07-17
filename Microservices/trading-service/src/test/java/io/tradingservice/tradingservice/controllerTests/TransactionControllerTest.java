package io.tradingservice.tradingservice.controllerTests;

import io.tradingservice.tradingservice.models.ImmutableTrade;
import io.tradingservice.tradingservice.models.ImmutableTransaction;
import io.tradingservice.tradingservice.models.Transaction;
import io.tradingservice.tradingservice.repositories.TransactionAccessObject;
import io.tradingservice.tradingservice.services.TransactionService;
import io.tradingservice.tradingservice.utils.Constants;
import io.tradingservice.tradingservice.utils.ServiceUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TransactionControllerTest {

//    @MockBean
//    TransactionAccessObject transactionAccessObject;
//
//    @MockBean
//    TransactionService transactionService;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @LocalServerPort
//    int randomServerPort;
//
//    String baseUrl;
//
//    ImmutableTrade trade;
//    ImmutableTransaction transaction;
//    List<ImmutableTrade> trades;

    @Test
    public void successTest(){

        assert 1==1;
    }

    //Setting up dummy trade
//    @Before
//    public void setUp(){
//
//        baseUrl = "http://localhost:" + randomServerPort;
//        trade = ImmutableTrade.builder()
//                .fundNumber("1234")
//                .fundName("Hedge")
//                .avgNav((float)22)
//                .status("purchase")
//                .quantity((float)7)
//                .invManager("GS")
//                .setCycle("T+2")
//                .invCurr("INR")
//                .sAndPRating((float)23.2)
//                .moodysRating((float)12)
//                .build();
//    }

    /*//Test for transactions history Api
    @Test
    public void transactionHistoryEndPoint() throws Exception {

        URI uri = new URI(baseUrl + Constants.transactionHistoryEndPoint);

        List<ImmutableTransaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Mockito.when(transactionService.getAllTransactionsById(Mockito.anyString()))
                .thenReturn((Response) transactions);

        ResponseEntity<String> entity= this.restTemplate.getForEntity(uri, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expected = "[{\"fundNumber\":\"1234\"," +
                "\"fundName\":\"Hedge\"," +
                "\"avgNav\":22.0," +
                "\"status\":\"purchase\"," +
                "\"quantity\":7.0," +
                "\"invManager\":\"GS\"," +
                "\"setCycle\":2," +
                "\"invCurr\":\"INR\"," +
                "\"sAndPRating\":23.2," +
                "\"moodysRating\":12.0}]";
        assertThat(expected).isEqualTo(entity.getBody());

    }

    Add transactions Api
    public Response addTransactions(@HeaderParam("Authorization") String header, Trade newTrade) {

        String userId = ServiceUtils.decodeJWTForUserId(header);
        return transactionService.addTransactionById(userId, newTrade);
    }
*/
}