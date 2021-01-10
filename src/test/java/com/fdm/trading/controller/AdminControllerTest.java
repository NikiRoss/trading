package com.fdm.trading.controller;

import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.events.RegistrationListener;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserService;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import com.fdm.trading.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    Principal principal;

    @MockBean
    UserServiceImpl mockUserService;

    @MockBean
    UserDao mockUserDao;

    @MockBean
    StockServiceImpl mockStockService;

    @MockBean
    RegistrationListener mockListner;
    User admin;
    User user;
    Stocks stocks;


    List<User> users;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        user = TestUtils.getUser(1l, "username", "email@email.com", "randomString");
        admin = TestUtils.getUser(2l, "admin", "email@email.com", "randomString");
        users = new ArrayList<>();
        users.add(user);
        stocks = new Stocks();
    }

    @Test
    @WithMockUser
    public void testDisableUser() throws Exception {
        when(mockUserService.findAllUsers()).thenReturn(users);
        when(mockUserService.findByUsername(any())).thenReturn(admin);
        when(mockUserService.findByUserId(any(Long.class))).thenReturn(user);
        doNothing().when(mockUserService).disableUser(user.getUsername());


        mockMvc.perform(get("/admin/disable/1").principal(principal)).andExpect(status().is2xxSuccessful());
        verify(mockUserService).findAllUsers();
    }

    @Test
    @WithMockUser
    public void testCreateNewAdmin() throws Exception {
        when(mockUserService.findByUsername(any())).thenReturn(admin);
        when(mockUserService.createNewUser(any(), any(), any())).thenReturn(user);
        mockMvc.perform(post("/admin/newadmin").principal(principal)).andExpect(status().is2xxSuccessful());
    }
    @Test
    @WithMockUser
    public void testExceptionEmailSentCreateNewAdminAdminExists() throws Exception {
        when(mockUserService.findByUsername(any())).thenReturn(admin);
        doNothing().when(mockListner).sendExceptionEmail(any(), any());
        when(mockUserService.createNewUser(any(), any(), any())).thenThrow(NameFormatException.class);
        mockMvc.perform(post("/admin/newadmin").principal(principal)).andExpect(status().is2xxSuccessful());
        verify(mockListner).sendExceptionEmail(any(), any());
    }

    @Test
    @WithMockUser
    public void testAddNewStock() throws Exception {
        when(mockStockService.createNewStock2(any())).thenReturn(stocks);
        when(mockUserService.findByUsername(any())).thenReturn(admin);
        mockMvc.perform(post("/admin/addstock").principal(principal)).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void testGetUsers() throws Exception {
        when(mockUserService.findAllUsers()).thenReturn(users);
        when(mockUserService.findByUsername(any())).thenReturn(admin);
        mockMvc.perform(get("/admin/users").principal(principal)).andExpect(status().is2xxSuccessful());
    }
}
