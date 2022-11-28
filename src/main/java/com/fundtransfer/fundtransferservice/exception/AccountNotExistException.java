package com.fundtransfer.fundtransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotExistException extends BusinessException {

    public AccountNotExistException(String message, String errorCode) {
        super(message, errorCode);
    }

    public AccountNotExistException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}
