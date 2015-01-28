package com.samenea.seapay.transaction.model.repository;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.Transaction;

import java.util.List;

public interface TransactionRepository extends BasicRepository<Transaction, String> {

	Transaction getByTransactionId(String transactionId);
    List<Transaction> getByTransactionStatus(TransactionStatus status);
}
