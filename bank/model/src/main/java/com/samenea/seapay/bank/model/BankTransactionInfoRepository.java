package com.samenea.seapay.bank.model;


import com.samenea.commons.component.model.BasicRepository;

/**
 * @author Jalal Ashrafi
 */
public interface BankTransactionInfoRepository extends BasicRepository<BankTransactionInfo,Long> {
    BankTransactionInfo findByTransactionId(String transactionId);
}
