package com.samenea.seapay.session.service;

import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IMerchant;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionService;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.model.SessionRepository;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.Transaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import com.samenea.seapay.transaction.service.TransactionBaseIntegrationServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import test.annotation.DataSets;

import static org.mockito.Mockito.*;

public class DefaultSessionServiceIntegrationTest extends TransactionBaseIntegrationServiceTest {
	@Autowired
    ISessionService sessionService;
    private IDGenerator idGenerator;
    @Autowired
    private IDGeneratorFactory idGeneratorFactory;
	@Autowired
	private IMerchantService merchantService;
	@Autowired
	private SessionRepository sessionRepository;
	@Autowired
	private ITransactionService transactionService;
	@Autowired
	private TransactionRepository transactionRepository;

	private final String merchantId = "M-100";
	private final String serviceId = "S-100";
	private final String password = "P-100";
	private final String customerId = "C-100";
	private final String transactionId = "100";
	private final int amount = 900;
	private final String description = "Desc-1";
	private final String callbackUrl = "http://www.google.com";
	private IMerchant merchant;
	private String bankName = "Mellat";
	private String accountNumber = "ACC-100";
    @Mock
    private IBankAccount bankAccount;
    @Mock
    private IOrder order;

    @Before
	public void before() {
        MockitoAnnotations.initMocks(this);
		when(merchantService.isMerchantValid(merchantId, serviceId, password)).thenReturn(true);
		merchant = mock(IMerchant.class);
		when(merchant.createOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(order);
        when(bankAccount.getBankName()).thenReturn(bankName);
        when(bankAccount.getAccountNumber()).thenReturn(accountNumber);

	}

	@Test
	@DataSets(setUpDataSet = "/sample-data/empty-sample-data.xml")
	public void should_persist_session_when_createSession_Called() throws RequestNotValidException {
		sessionService.createSession(merchantId, serviceId, password);
		Assert.assertEquals(1L, sessionService.getAll().size());
	}

	@Test
	@DataSets(setUpDataSet = "/sample-data/empty-sample-data.xml")
	public void created_transaction_should_update_in_database_after_bank_mapping() throws RequestNotValidException {
        idGenerator = mock(IDGenerator.class);
		when(idGenerator.getNextID()).thenReturn(transactionId);
        when(idGeneratorFactory.getIDGenerator(anyString())).thenReturn(idGenerator);
		when(merchantService.getMerchant(anyString())).thenReturn(merchant);
		ISession session = sessionService.createSession(merchantId, serviceId, password);
		Transaction transaction = (Transaction) session.createTransaction(amount, description, callbackUrl, customerId);
		transaction.mapToBank(bankAccount);
		transaction = transactionRepository.getByTransactionId("TRN-"+transactionId);

		Assert.assertEquals(TransactionStatus.BANK_RESOLVED, transaction.getStatus());

	}

}
