package com.fundtransfer.fundtransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class InSufficientBalanceException extends BusinessException {

    public InSufficientBalanceException(String message, String errorCode) {
        super(message, errorCode);
    }

    public InSufficientBalanceException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}