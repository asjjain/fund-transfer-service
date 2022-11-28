package com.fundtransfer.fundtransferservice.service;


import com.fundtransfer.fundtransferservice.entity.Account;
import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import com.fundtransfer.fundtransferservice.enums.TransactionStatus;
import com.fundtransfer.fundtransferservice.exception.AccountNotExistException;
import com.fundtransfer.fundtransferservice.repository.AccountRepository;
import com.fundtransfer.fundtransferservice.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    AccountRepository mockAccountRepository;

    private TransferRequest mockTransferRequest;
    private Account mockAccount;
    private Account mockDebitAccount;
    private Account mockCreditAccount;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup(){
        this.mockAccount= new Account(1L, new BigDecimal(100.00), Currency.getInstance("USD"));
        this.mockDebitAccount= new Account(1L, new BigDecimal(100.00), Currency.getInstance("USD"));
        this.mockCreditAccount= new Account(2L, new BigDecimal(100.00), Currency.getInstance("USD"));
        this.mockTransferRequest = new TransferRequest(null,1L,2L,new BigDecimal(10.00),Currency.getInstance("USD"), "", TransactionStatus.SUCCESSFUL);

        mockMvc = MockMvcBuilders.standaloneSetup(accountService).build();
    }

    @AfterEach
    void tearDown(){
        this.mockAccount= null;
        this.mockDebitAccount=null;
        this.mockCreditAccount=null;
    }

    @Test
    public void getAccountBalanceByAccountIdShouldReturnRespectiveAccount() throws Exception{
        when(mockAccountRepository.findById(any())).thenReturn(Optional.of(mockAccount));

        Account account = accountService.retrieveBalance(1L);
        Assertions.assertNotNull(account);
        Assertions.assertEquals(account, mockAccount);
        verify(mockAccountRepository).findById(any());
        verify(mockAccountRepository, times(1)).findById(any());
    }

    @Test
    public void givenInvalidAccountIdThenThrowAccountNotExistException() throws Exception{

        when(mockAccountRepository.findById(123L)).thenReturn(Optional.ofNullable(null));
        AccountNotExistException exception = Assertions.assertThrows(AccountNotExistException.class, ()-> accountService.retrieveBalance(123L));
        Assertions.assertEquals("Account with id:123 does not exist.", exception.getMessage());
        verify(mockAccountRepository).findById(any());
        verify(mockAccountRepository, times(1)).findById(any());
    }

    @Test
    public void givenTransferRequestShouldTransferBalance() throws Exception{
        when(mockAccountRepository.getAccountForUpdate(1L)).thenReturn(Optional.of(mockDebitAccount));
        when(mockAccountRepository.getAccountForUpdate(2L)).thenReturn(Optional.of(mockDebitAccount));
        TransferRequest transferRequest= accountService.transferBalances(mockTransferRequest);

        Assertions.assertNotNull(transferRequest);
        Assertions.assertEquals(transferRequest, mockTransferRequest);
        verify(mockAccountRepository, times(2)).getAccountForUpdate(any());
    }


}
