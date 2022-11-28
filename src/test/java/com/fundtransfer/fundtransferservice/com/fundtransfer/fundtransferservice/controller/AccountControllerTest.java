package com.fundtransfer.fundtransferservice.com.fundtransfer.fundtransferservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundtransfer.fundtransferservice.client.ExchangeRateApiClient;
import com.fundtransfer.fundtransferservice.constant.ErrorCode;
import com.fundtransfer.fundtransferservice.controller.AccountController;
import com.fundtransfer.fundtransferservice.entity.Account;
import com.fundtransfer.fundtransferservice.exception.AccountNotExistException;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import com.fundtransfer.fundtransferservice.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Currency;

import static com.fundtransfer.fundtransferservice.com.fundtransfer.fundtransferservice.controller.ExchangeRateApiControllerTest.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private AccountService accountService;
    private Account account;

    @InjectMocks
    private AccountController accountController;


    @BeforeEach
    public void setup(){
        this.account= new Account(1L, new BigDecimal(100.00), Currency.getInstance("USD"));
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @AfterEach
    void tearDown(){
        this.account= null;
    }


    @Test
    public void getAccountBalanceByAccountIdShouldReturnRespectiveAccount() throws Exception{
        when(accountService.retrieveBalance(1L  )).thenReturn(account);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/1/balances").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account))).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(100.00));

        verify(accountService).retrieveBalance(any());
        verify(accountService, times(1)).retrieveBalance(any());

    }

    @Test
    public void shouldReturn404WhenAccountIdInRequestNotExist() throws Exception{
        when(accountService.retrieveBalance(123L  )).thenThrow(new AccountNotExistException("Account with id:123 does not exist.", ErrorCode.ACCOUNT_ERROR, HttpStatus.NOT_FOUND));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/123/balances").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());

        verify(accountService).retrieveBalance(any());
        verify(accountService, times(1)).retrieveBalance(any());
    }





    public static String  asJsonString(final Object obj) {
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
