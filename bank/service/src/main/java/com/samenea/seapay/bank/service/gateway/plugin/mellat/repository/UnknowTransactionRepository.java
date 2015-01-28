package com.samenea.seapay.bank.service.gateway.plugin.mellat.repository;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransactionStatus;

import java.util.List;

public interface UnknowTransactionRepository extends BasicRepository<UnknownTransaction, Long> {
    UnknownTransaction findByTransactionId(String transactionId);
    List<UnknownTransaction> findByStatus(UnknownTransactionStatus status);

}
