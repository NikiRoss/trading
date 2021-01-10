package com.fdm.trading.controller;

import com.fdm.trading.domain.*;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsControllerTest {

    @Mock
    Principal principal;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StockServiceImpl mockStockService;

    @MockBean
    UserServiceImpl mockUserService;

    @MockBean
    TransactionServiceImpl mockTransactionService;

    @Mock
    User user;

    Account account;

    StockListEntity sle;

    Stocks stocks;

    Transaction transaction;

    List<StockListEntity> stocksListEntities = new ArrayList<>();
    List<Stocks> stocksList = new ArrayList<>();
    List<Transaction> transactionList = new ArrayList<>();

    @Before
    public void setUp(){
        account = new Account();
        account.setAccountId(1l);
        sle = new StockListEntity();
        sle.setStockId(1l);
        sle.setVolume(1l);
        stocksListEntities.add(sle);
        stocks = new Stocks();
        stocks.setCompany("company");
        transaction = new Transaction();
        transaction.setStocks(stocks);
        transactionList.add(transaction);
    }

    @Test
    @WithMockUser
    public void testGetHoldings() throws Exception {
        when(mockTransactionService.findAllTransactions(anyLong())).thenReturn(transactionList);
        when((User)mockUserService.loadUserByUsername(any())).thenReturn(user);
        when(user.getAccount()).thenReturn(account);

        mockMvc.perform(get("/transactions").principal(principal)).andExpect(status().is2xxSuccessful());

    }

}
