package com.fundtransfer.fundtransferservice.entity;

import com.fundtransfer.fundtransferservice.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfer_request")
public class TransferRequest {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transferRequestId;
    private Long accountFromId;
    private Long accountToId;
    private BigDecimal amount;
    private Currency currency;
    private String description = "";
    private TransactionStatus status;


}
