package com.fundtransfer.fundtransferservice.repository;

import com.fundtransfer.fundtransferservice.entity.TransferRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<TransferRequest, Long> {


}
