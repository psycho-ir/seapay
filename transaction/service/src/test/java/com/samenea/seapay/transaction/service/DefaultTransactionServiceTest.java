package com.samenea.seapay.transaction.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.session.model.SessionFactory;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.model.ExtraApplicationData;
import com.samenea.seapay.transaction.model.Transaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import static org.mockito.Mockito.*;

public class DefaultTransactionServiceTest extends TransactionBaseServiceTest {
	private final String merchantId = "merchatId";
	private final String username = "username";
	private final String password = "password";
	private final String bankName = "bankName";
	private final String serviceId = "serviceId";
	private final String accountNumber = "accountNumber";
	private final int amount = 900;
	ExtraApplicationData extraApplicationData;

	@Autowired
	IMerchantService merchantService;

	@Autowired
	ITransactionService transactionService;

	@Before
	public void before() {
		reset(merchantService);
	}

	@Test
	public void empty() {
		System.out.println(transactionService == null);

	}

}
