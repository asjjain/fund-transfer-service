package com.fundtransfer.fundtransferservice.service;

import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import com.fundtransfer.fundtransferservice.enums.TransactionStatus;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import com.fundtransfer.fundtransferservice.repository.TransactionRepository;
import com.fundtransfer.fundtransferservice.service.impl.FundTransferServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FundTransferServiceTest {

    @Autowired
    private MockMvc mockMvc;

    private TransferRequest mockTransferRequest;
    private ExchangeResponse mockExchangeResponse;


    @Mock
    TransactionRepository mockTransactionRepository;


    @InjectMocks
    FundTransferServiceImpl fundTransferService;


    @BeforeEach
    public void setup(){
        this.mockTransferRequest = new TransferRequest(null,1L,2L,new BigDecimal(10.00), Currency.getInstance("USD"), "", TransactionStatus.SUCCESSFUL);
        mockMvc = MockMvcBuilders.standaloneSetup(fundTransferService).build();
    }

    @AfterEach
    void tearDown(){
        this.mockTransferRequest=null;
        this.mockExchangeResponse=null;
    }


    @Test
    public void givenTransferRequestToAddShouldSavedRoleSuccessfully(){

        when(mockTransactionRepository.save(any())).thenReturn(mockTransferRequest);
        TransferRequest transferRequest= fundTransferService.transferBalancesRequest(mockTransferRequest);

        Assertions.assertNotNull(transferRequest);
        verify(mockTransactionRepository).save(any());
        verify(mockTransactionRepository, times(1)).save(any());
    }

}
