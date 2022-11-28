package com.fundtransfer.fundtransferservice.service.impl;

import com.fundtransfer.fundtransferservice.client.ExchangeRateApiClient;
import com.fundtransfer.fundtransferservice.constant.ErrorCode;
import com.fundtransfer.fundtransferservice.entity.*;
import com.fundtransfer.fundtransferservice.enums.TransactionStatus;
import com.fundtransfer.fundtransferservice.exception.*;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import com.fundtransfer.fundtransferservice.repository.AccountRepository;
import com.fundtransfer.fundtransferservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ExchangeRateApiClient exchangeRateApiClient;

    @Override
    public Account retrieveBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistException("Account with id:" + accountId + " does not exist.", ErrorCode.ACCOUNT_ERROR, HttpStatus.NOT_FOUND));

        return account;
    }

    @Transactional
    public TransferRequest transferBalances(TransferRequest transferRequest) throws InSufficientBalanceException, AccountNotExistException {
        Account accountFrom = accountRepository.getAccountForUpdate(transferRequest.getAccountFromId())
                .orElseThrow(() -> {
                        transferRequest.setStatus(TransactionStatus.WRONG_DATA);
                         return   new AccountNotExistException("Account with id:" + transferRequest.getAccountFromId() + " does not exist.", ErrorCode.ACCOUNT_ERROR, HttpStatus.BAD_REQUEST);
                });

        Account accountTo = accountRepository.getAccountForUpdate(transferRequest.getAccountToId())
                .orElseThrow(() -> {
                    transferRequest.setStatus(TransactionStatus.WRONG_DATA);
                    return  new AccountNotExistException("Account with id:" + transferRequest.getAccountToId() + " does not exist.", ErrorCode.ACCOUNT_ERROR, HttpStatus.BAD_REQUEST);
                });

        BigDecimal amountToDebit = transferRequest.getAmount();
        BigDecimal amountToCredit= transferRequest.getAmount();

        if(transferRequest.getCurrency() != accountFrom.getAccountCurrency() || transferRequest.getCurrency() != accountTo.getAccountCurrency()){
            //fetch the exchange rates.
            log.info("Fetching the exchange rates...");
            ExchangeResponse exchangeResponse= exchangeRateApiClient.getExchangeRateFromCurrencyCode(transferRequest.getCurrency().getCurrencyCode());

            Map<String, Double> rates= exchangeResponse.getRates();
            Double accountFromCurrencyRate= rates.get(accountFrom.getAccountCurrency().getCurrencyCode());
            Double accountToCurrencyRate=   rates.get(accountTo.getAccountCurrency().getCurrencyCode());

            //calculate the transaction amount
            amountToDebit=amountToDebit.multiply(new BigDecimal(accountFromCurrencyRate));
            amountToCredit=amountToCredit.multiply(new BigDecimal(accountToCurrencyRate));
            log.info("amountToDebit:: " + amountToDebit);
            log.info("amountToCredit:: " + amountToCredit);

        }

        if(accountFrom.getAccountBalance().compareTo(amountToDebit) < 0) {
            transferRequest.setStatus(TransactionStatus.WRONG_DATA);
            throw new InSufficientBalanceException("Account with id:" + accountFrom.getAccountId() + " does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR, HttpStatus.PRECONDITION_FAILED);
        }

        accountFrom.setAccountBalance(accountFrom.getAccountBalance().subtract(amountToDebit));
        accountTo.setAccountBalance(accountTo.getAccountBalance().add(amountToCredit));

        transferRequest.setStatus(TransactionStatus.SUCCESSFUL);
        return transferRequest;
    }


}
