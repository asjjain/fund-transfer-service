package com.fundtransfer.fundtransferservice.service;

import com.fundtransfer.fundtransferservice.entity.TransferRequest;

public interface FundTransferService {

    public TransferRequest transferBalancesRequest(TransferRequest transfer);
}
