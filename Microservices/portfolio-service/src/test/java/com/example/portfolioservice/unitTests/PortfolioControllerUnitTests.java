package com.example.portfolioservice.unitTests;
import com.example.portfolioservice.models.*;
import static com.example.portfolioservice.utils.Constants.SECRET_TOKEN;
import static org.junit.Assert.assertEquals;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Optional;
import com.example.portfolioservice.service.PortfolioService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PortfolioControllerUnitTests {
    @MockBean
    private PortfolioService portfolioService;

    @LocalServerPort
    private int randomServerPort;

    private TestRestTemplate restTemplate;

    private String baseURL;
    private String token;
    private UserParser userParser;
    private ImmutableUser userDB;
    private BalanceInfo balanceInfo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initialize() {
        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb21idWRkaGEyMCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVFJBREVSIl0sImlhdCI6MTU2M"
                + "jIzNjY4OSwibmFtZSI6IlNvbWJ1ZGRoYSBDaGFrcmF2YXJ0aHkiLCJleHAiOjE1NjIyNDAyODl9.oct7z-0NzNkgXbqW6mipjki"
                + "A6_UsLnIrO_yDYZVX8TvM5MqftwN525gtdVYtO45jbYmqFT74DXanArHkYEGLoQ";
        restTemplate = new TestRestTemplate();
        List<Fund> funds = new ArrayList<>();
        balanceInfo = new BalanceInfo();
        balanceInfo.setCurrBal(34);
        balanceInfo.setBaseCurr("INR");
        baseURL = "http://localhost:" + randomServerPort + "/";
        userDB = ImmutableUser.builder()
                .userId("1")
                .currBal(34)
                .all_funds(funds)
                .baseCurr("INR").build();

        userParser = ImmutableUserParser.builder()
                .userId("1")
                .currBal(34)
                .all_funds(funds)
                .baseCurr("INR").build();
    }

    @Test
    public void test_getUserById() throws Exception {
        URI uri = new URI(baseURL);
        Mockito.when(portfolioService.getUser(Mockito.anyString())).thenReturn(Optional.of(userDB));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        String expected = "{\"present\":true}";
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(entity.getBody(), expected);
    }

    @Test
    public void test_getBalanceById() throws Exception {
        URI uri = new URI(baseURL + "/getBalance");
        Mockito.when(portfolioService.getBalanceById(Mockito.anyString())).thenReturn(Optional.of(balanceInfo));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        String expected = "{\"baseCurr\":\"INR\",\"currBal\":34.0}";
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(entity.getBody(), expected);
    }

    @Test
    public void test_updateBaseCurrency()throws Exception {
        String newCurr = "USD";
        URI uri = new URI(baseURL + "/update/baseCurrency/?Currency="+newCurr);
        Mockito.when(portfolioService.updateBaseCurrency(Mockito.anyString(), Mockito.anyString())).thenReturn(newCurr);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.PATCH, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(entity.getBody(), newCurr);
    }

    @Test
    public void test_addUserSecretKeyWrong()throws Exception {
        URI uri = new URI(baseURL + "/add/user?secret=" + "wrong" + "&balance="+ userParser.currBal()+
                "&baseCurr="+ userParser.baseCurr().get());
        Mockito.when(portfolioService.createUser(Mockito.any(UserParser.class))).thenReturn("User Created");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_addExistingUser()throws Exception {
        URI uri = new URI(baseURL + "/add/user?secret=" + SECRET_TOKEN + "&balance="+ userParser.currBal()+
                "&baseCurr="+ userParser.baseCurr().get());
        Mockito.when(portfolioService.createUser(Mockito.any(UserParser.class))).thenReturn("User Created");
        Mockito.when(portfolioService.getUser(Mockito.anyString())).thenReturn(Optional.of(userDB));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_addUser()throws Exception {
        URI uri = new URI(baseURL + "/add/user?secret=" + SECRET_TOKEN + "&balance="+ userParser.currBal()+
                "&baseCurr="+ userParser.baseCurr().get());

        Mockito.when(portfolioService.createUser(Mockito.any(UserParser.class))).thenReturn("User Created");
        Optional<User> user = Optional.absent();
        Mockito.when(portfolioService.getUser(Mockito.anyString())).thenReturn(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void test_updateBalance()throws Exception {
        float newBal = 34.56f;
        URI uri = new URI(baseURL + "/update?balance=" + userParser.currBal() + "&secret=" + SECRET_TOKEN);
        Mockito.when(portfolioService.updateBalance(Mockito.anyString(), Mockito.anyFloat())).thenReturn(newBal);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.PATCH, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void test_deleteUserById() throws Exception {
        Optional<User> user = Optional.of(userDB);
        URI uri = new URI(baseURL + "/delete/1");
        Mockito.when(portfolioService.delete(Mockito.anyString())).thenReturn(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void test_updateUser() throws Exception {
        Optional<User> user = Optional.of(userDB);
        URI uri = new URI(baseURL + "/update/userParser/?secret=" + SECRET_TOKEN);
        Mockito.when(portfolioService.update(Mockito.any())).thenReturn(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");
        HttpEntity<UserParser> request = new HttpEntity<>(this.userParser, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.PATCH, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void test_getFunds() throws Exception {
        URI uri = new URI(baseURL + "/getFunds/");
        Mockito.when(portfolioService.getUser(Mockito.anyString())).thenReturn(Optional.of(userDB));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(entity.getBody(), "[]");

    }
}