package com.fundtransfer.fundtransferservice.controller;

import com.fundtransfer.fundtransferservice.client.ExchangeRateApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.Currency;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateApiController {

    private final ExchangeRateApiClient exchangeRateApiClient;

    @GetMapping(value = "/exchangeRate/{currenyCode}")
    public ResponseEntity readExchangeRateData(@PathVariable(required = true) String currenyCode) {
        log.info(currenyCode);


        if (Currency.getAvailableCurrencies().stream().noneMatch(c-> c.getCurrencyCode()
                .equals(currenyCode))) {
            log.error("Invalid currency code :" + currenyCode);
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("valid Currency Code is requried");
        }
        return ResponseEntity.ok(exchangeRateApiClient.getExchangeRateFromCurrencyCode(currenyCode));
    }
}
