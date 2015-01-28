package com.samenea.seapay.transaction.model;

import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionFactoryTest extends TransactionBaseModelTest {

	private final String transactionId = "111";
	@Autowired
	TransactionRepository transactionRepository;
	private final int amount = 900;
	private final String description = "Description";

    private IDGenerator idGenerator;
    @Autowired
    private IDGeneratorFactory idGeneratorFactory;

	private final String idToken = "TRN-";

	@Before
	public void before() {
		mockTransactionRepository();
        idGenerator = mock(IDGenerator.class);
        when(idGenerator.getNextID()).thenReturn(transactionId);
        when(idGeneratorFactory.getIDGenerator(idToken)).thenReturn(idGenerator);
	}

	@Test
	public void should_persist_transaction_after_creation() throws RequestNotValidException {
		final ITransactionFactory transactionFactory = new TransactionFactory();
		transactionFactory.createTransaction(amount, description);
		verify(transactionRepository).store(any(Transaction.class));
	}

	@Test
	public void session_should_create_transaction_and_set_its_transactionId() throws RequestNotValidException {
		final ITransactionFactory transactionFactory = new TransactionFactory();
		final ITransaction transaction = transactionFactory.createTransaction(amount, description);
		Assert.assertNotNull(transaction);
		Assert.assertEquals(idToken +transactionId, transaction.getTransactionId());

	}

	private void mockTransactionRepository() {
		reset(transactionRepository);
		when(transactionRepository.store(any(Transaction.class))).thenAnswer(new Answer<Transaction>() {
			@Override
			public Transaction answer(InvocationOnMock invocation) throws Throwable {
				return (Transaction) invocation.getArguments()[0];
			}
		});
	}

}
