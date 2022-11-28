package com.fundtransfer.fundtransferservice.service.impl;

import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import com.fundtransfer.fundtransferservice.repository.TransactionRepository;
import com.fundtransfer.fundtransferservice.service.FundTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FundTransferServiceImpl implements FundTransferService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransferRequest transferBalancesRequest(TransferRequest transferRequest) {

        return  transactionRepository.save(transferRequest);

    }
}
