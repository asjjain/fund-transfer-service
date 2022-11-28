package com.fundtransfer.fundtransferservice.client;

import com.fundtransfer.fundtransferservice.constant.ErrorCode;
import com.fundtransfer.fundtransferservice.exception.SystemException;
import com.fundtransfer.fundtransferservice.model.ExchangeResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}")
public interface ExchangeRateApiClient {


    @GetMapping("/{currencyCode}")
    @CircuitBreaker(name = "getExchangeRateCB", fallbackMethod = "getExchangeRateFromCurrencyCodeFallBack")
    ExchangeResponse getExchangeRateFromCurrencyCode(@PathVariable String currencyCode);

    default ExchangeResponse getExchangeRateFromCurrencyCodeFallBack(String currencyCode, Exception ex) throws SystemException {

        throw new SystemException("Not able to fetch Exchage rate, Please try after sometime!", ErrorCode.SYSTEM_ERROR);

    }

}
