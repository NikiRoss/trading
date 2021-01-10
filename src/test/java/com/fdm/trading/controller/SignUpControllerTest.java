package com.fdm.trading.controller;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.dao.VerificationTokenDao;
import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import com.fdm.trading.service.tokenService.VerificationTokenServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import com.fdm.trading.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    VerificationTokenDao tokenDao;

    @Mock
    AuthoritiesDao mockAuthoritiesDao;

    @MockBean
    UserServiceImpl mockUserService;

    @MockBean
    VerificationTokenServiceImpl mockVerificationTokenService;

    @Mock
    VerificationTokenDao mockTokenDao;

    @Mock
    ApplicationEventPublisher mockEventPublisher;

    @Mock
    PasswordEncoder mockPasswordEncoder;

    @Mock
    User user;


    VerificationToken verificationToken;


    Date mockDate;

    Calendar calendar;

    @Mock
    UserDao mockuserDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        calendar = Calendar.getInstance();
        verificationToken = new VerificationToken();
        verificationToken.setExpiry(calendar.getTime());
        mockDate = new Date();
        mockDate.setTime(calendar.getTime().getTime()+1);
        verificationToken.setToken("token");
        verificationToken.setUser(user);
        verificationToken.setId(1l);
    }

    @Test
    public void testSignUpReturnsSignUpPage() throws Exception {
        mockMvc.perform(get("/signup")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void testPostUserRegistration() throws Exception {
        Mockito.doNothing().when(mockEventPublisher).publishEvent(any());
        when(mockPasswordEncoder.encode(any())).thenReturn("any");
        Mockito.doNothing().when(mockUserService).save(any());
        when(mockAuthoritiesDao.save(any())).thenReturn(user);
        mockMvc.perform(post("/signup/user/registration")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void testConfirmRegistration() throws Exception {
        when(mockUserService.getVerificationToken(any())).thenReturn(verificationToken);
        when(mockVerificationTokenService.tokenIsInvalid(any())).thenReturn(false);
        when(mockVerificationTokenService.tokenHasExpired(any(), any())).thenReturn(false);
        doNothing().when(mockUserService).save(any());
        mockMvc.perform(get("/registrationConfirm?token=token")).andExpect(status().is3xxRedirection());
    }

}
