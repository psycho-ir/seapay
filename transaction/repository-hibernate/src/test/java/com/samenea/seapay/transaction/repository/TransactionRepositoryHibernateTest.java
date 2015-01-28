package com.samenea.seapay.transaction.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.annotation.DataSets;

import com.samenea.seapay.transaction.model.Transaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;

public class TransactionRepositoryHibernateTest extends TransactionBaseRepositoryTest {
	private final String transactionId = "TRN-111";
	private final String serviceId = "123";
	private final int amount = 900;
	private final String description = "description";
	private final String bankName = "mellat";
	private final String accountNumber = "1111";
	@Autowired
	TransactionRepository transactionRepository;

	@Test(expected = NotFoundException.class)
	@DataSets(setUpDataSet="/sample-data/empty-sample-data.xml")
	public void should_throw_NotFoundException_when_there_is_no_such_transaction() {
		transactionRepository.getByTransactionId(transactionId);
	}

	@Test
	@DataSets(setUpDataSet="/sample-data/transaction-exist-sample-data.xml")
	public void should_find_transaction_from_its_transactionId() {
		Transaction transaction = transactionRepository.getByTransactionId(transactionId);
		Assert.assertNotNull(transaction);
	}

	@Test
	public void should_store() {
		transactionRepository.store(new Transaction("transactionId", 90, "description"));
	}
}
