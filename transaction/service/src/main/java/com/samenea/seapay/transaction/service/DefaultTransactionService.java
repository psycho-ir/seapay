package com.samenea.seapay.transaction.service;

import java.util.List;

import com.samenea.seapay.transaction.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;

@Service
@Transactional
public class DefaultTransactionService implements ITransactionService {


	@Autowired
	private TransactionRepository transactionRepository;

    @Override
    public List<? extends ITransaction> findTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.getByTransactionStatus(status);
    }

    @Override
	public ITransaction getTransaction(String transactionId) {
		return transactionRepository.getByTransactionId(transactionId);
	}

}
