package com.fundtransfer.fundtransferservice.controller;

import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import com.fundtransfer.fundtransferservice.exception.*;
import com.fundtransfer.fundtransferservice.service.AccountService;
import com.fundtransfer.fundtransferservice.service.FundTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/fundTrasfer")
@Api(tags = {"Fund Transfer Controller"}, description = "Provide APIs for fund transfer related operation")
@Slf4j
public class FundTransferController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private FundTransferService fundTransferService;

    @PostMapping(consumes = { "application/json" })
    @ApiOperation(value = "API to create transaction",  produces = "application/json")
    public ResponseEntity transferMoney(@RequestBody /*@Valid*/ TransferRequest transferRequest) throws Exception {

        try {
            transferRequest= fundTransferService.transferBalancesRequest(transferRequest);
            transferRequest=accountService.transferBalances(transferRequest);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (AccountNotExistException | InSufficientBalanceException e) {
            log.error("Fail to transfer balances, please check with system administrator.");
            throw e;
        }
    }


}
