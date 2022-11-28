package com.fundtransfer.fundtransferservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class AccountDto {

    private Long id;
    private String name;
    private BigDecimal balance;
    private Currency currency;
    private Long version;
}
