package com.fundtransfer.fundtransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CheckBalanceException extends SystemException {

    public CheckBalanceException(String message, String errorCode) {
        super(message, errorCode);
    }

}