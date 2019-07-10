package com.sapient.authservice.unitTests;

import com.sapient.authservice.dao.UserDAO;
import com.sapient.authservice.entities.ImmutableUser;
import com.sapient.authservice.entities.ParsedUser;
import com.sapient.authservice.entities.User;
import com.sapient.authservice.service.UserDetailsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthServiceUnitTests {

    @Mock
    UserDAO dao;

    @InjectMocks
    UserDetailsServiceImpl service;

    @LocalServerPort
    int port;

    User user;
    User userDB;
    String baseUrl;
    UserDetails userDetails;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void mockDB(){
//        baseUrl = "http://localhost:" + port + "/auth";
        user = ImmutableUser.builder().userId("harsh123").password("qwerty123").role("ROLE_TRADER").fullName("Harsh Jaiswal").build();
        userDB = ImmutableUser.builder().userId("harsh123").password("qwerty123").role("ROLE_TRADER").fullName("Harsh Jaiswal").build();
    }

    @Test
    public void findByUserIdTest(){
        Mockito.when(dao.getUserByUserId(Mockito.any(String.class))).thenReturn((ImmutableUser) user);
        Assert.assertEquals(userDB, service.findByUserId("harsh123"));
    }
}
