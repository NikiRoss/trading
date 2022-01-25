package com.fdm.trading.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @MockBean
   private UserServiceImpl userService;

   @MockBean
   private StockServiceImpl stockService;

   @Mock
   private User user;

   @Mock
   private UserDetails userDetails;


   private Account account;


   @Before
   public void init(){
       account = new Account();
       MockitoAnnotations.initMocks(this);


   }
    @Test
    public void testRootRedirectsToLoginPage() throws Exception {
        when(userService.findByUsername("any")).thenReturn(user);
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testLoginLoginPageSuccess() throws Exception {
        when(userService.findByUsername("any")).thenReturn(user);
        mockMvc.perform(get("/login")).andExpect(status().is2xxSuccessful());
    }
    @Test
    @WithMockUser
    public void testUserHomePageSuccess() throws Exception {
        when((User) userService.loadUserByUsername(any())).thenReturn(user);
        when(user.getAccount()).thenReturn(account);
        mockMvc.perform(get("/account")).andExpect(status().is2xxSuccessful());
    }
}
