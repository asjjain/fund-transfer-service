package com.fundtransfer.fundtransferservice.com.fundtransfer.fundtransferservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundtransfer.fundtransferservice.client.ExchangeRateApiClient;
import com.fundtransfer.fundtransferservice.constant.ErrorCode;
import com.fundtransfer.fundtransferservice.controller.FundTransferController;
import com.fundtransfer.fundtransferservice.entity.Account;
import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import com.fundtransfer.fundtransferservice.enums.TransactionStatus;
import com.fundtransfer.fundtransferservice.exception.AccountNotExistException;
import com.fundtransfer.fundtransferservice.exception.InSufficientBalanceException;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import com.fundtransfer.fundtransferservice.service.AccountService;
import com.fundtransfer.fundtransferservice.service.FundTransferService;
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
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FundTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FundTransferService fundTransferService;
    @Mock
    private AccountService accountService;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    private TransferRequest mockTransferRequest;
    private Account mockDebitAccount;
    private Account mockCreditAccount;
    private ExchangeResponse mockExchangeResponse;

    @InjectMocks
    private FundTransferController fundTransferController;

    @BeforeEach
    public void setup(){
        this.mockTransferRequest = new TransferRequest(null,1L,2L,new BigDecimal(10.00),Currency.getInstance("USD"), "", TransactionStatus.SUCCESSFUL);
        this.mockDebitAccount= new Account(1L, new BigDecimal(100.00), Currency.getInstance("USD"));
        this.mockCreditAccount= new Account(2L, new BigDecimal(100.00), Currency.getInstance("EUR"));
        this.mockExchangeResponse = new ExchangeResponse("success", 1669422061, LocalDate.now().toString(),  "https://www.exchangerate-api.com", 0,
                "https://www.exchangerate-api.com/docs/free", new HashMap<String,Double >(),
                1669334551, "USD",LocalDate.now().toString(),"https://www.exchangerate-api.com/terms" );
        mockMvc = MockMvcBuilders.standaloneSetup(fundTransferController).build();
    }

    @AfterEach
    void tearDown(){
        this.mockTransferRequest=null;
        this.mockDebitAccount= null;
        this.mockCreditAccount= null;
        this.mockExchangeResponse=null;
    }

    @Test
    public void PostMappingOfFundTransferRequest() throws Exception{
        when(fundTransferService.transferBalancesRequest(any()) ).thenReturn(mockTransferRequest);
        when(accountService.transferBalances(any()) ).thenReturn(mockTransferRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.post ("/api/v1/fundTrasfer").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockTransferRequest))).andDo(MockMvcResultHandlers.print()).andExpect(status().isAccepted());

        verify(accountService).transferBalances(any());
        verify(fundTransferService).transferBalancesRequest(any());
        verify(fundTransferService, times(1)).transferBalancesRequest(any());
        verify(accountService, times(1)).transferBalances(any());

    }


    @Test
    public void shouldReturn400WhenAccountIdInRequestNotExist() throws Exception{
        when(fundTransferService.transferBalancesRequest(any()) ).thenReturn(mockTransferRequest);
        when(accountService.transferBalances(any()) ).thenThrow(new AccountNotExistException("Account with id: 1 does not exist.", ErrorCode.ACCOUNT_ERROR, HttpStatus.BAD_REQUEST));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fundTrasfer").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockTransferRequest))).andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());
        verify(accountService).transferBalances(any());
        verify(fundTransferService).transferBalancesRequest(any());
        verify(fundTransferService, times(1)).transferBalancesRequest(any());
        verify(accountService, times(1)).transferBalances(any());
    }

    @Test
    public void shouldReturn412WhenInSufficientBalance() throws Exception{
        when(fundTransferService.transferBalancesRequest(any()) ).thenReturn(mockTransferRequest);
        when(accountService.transferBalances(any()) ).thenThrow(new InSufficientBalanceException("Account with id: 1 does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR, HttpStatus.PRECONDITION_FAILED));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fundTrasfer").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockTransferRequest))).andDo(MockMvcResultHandlers.print()).andExpect(status().is4xxClientError());
        verify(accountService).transferBalances(any());
        verify(fundTransferService).transferBalancesRequest(any());
        verify(fundTransferService, times(1)).transferBalancesRequest(any());
        verify(accountService, times(1)).transferBalances(any());
    }

    public static String  asJsonString(final Object obj) {
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
