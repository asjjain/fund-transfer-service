package com.fundtransfer.fundtransferservice.controller;

import com.fundtransfer.fundtransferservice.entity.Account;
import com.fundtransfer.fundtransferservice.service.AccountService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@Api(tags = { "Accounts Controller" }, description = "Provide APIs for account related operation")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}/balances")
    @ApiOperation(value = "Get account balance by id", response = Account.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Account not found with ID")})
    public Account getBalance(
            @ApiParam(value = "ID related to the account", required = true) @PathVariable Long accountId) {
        return accountService.retrieveBalance(accountId);
    }


}
