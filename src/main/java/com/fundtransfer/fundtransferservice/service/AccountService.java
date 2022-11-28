package com.fundtransfer.fundtransferservice.service;

import com.fundtransfer.fundtransferservice.entity.Account;
import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface AccountService {


    Account retrieveBalance(Long accountId);

    public TransferRequest transferBalances(TransferRequest transfer) throws NoSuchFieldException, IllegalAccessException;
}
