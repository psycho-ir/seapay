package com.samenea.seapay.transaction.model.session;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;


import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionFactory;
import com.samenea.seapay.session.model.Session;
import com.samenea.seapay.session.model.SessionFactory;
import com.samenea.seapay.session.model.SessionRepository;
import com.samenea.seapay.transaction.model.TransactionBaseModelTest;
import com.samenea.seapay.transaction.model.ITransactionFactory;

import static org.mockito.Mockito.*;

public class DefaultSessionFactoryTest extends TransactionBaseModelTest {
	ISessionFactory sessionFactory;
	@Autowired
	ITransactionFactory transactionFactory;
	@Autowired
	IMerchantService merchantService;
	@Autowired
	SessionRepository sessionRepository;

	private final String merchantId = "M-1111";
	private final String serviceId = "SRV-111";
	private final String password = "123";

	

	@Before
	public void before() {
		reset(merchantService);
		when(merchantService.isMerchantValid(merchantId, serviceId, password)).thenReturn(true);
		sessionFactory = new SessionFactory();
		
		mockSessionRepository();

		
		
	}

	@Test
	public void should_create_new_session() throws RequestNotValidException {
		ISession session = sessionFactory.createSession(merchantId, serviceId, password);

		Assert.assertNotNull(session);
	}

	@Test
	public void created_session_properties_should_set() throws RequestNotValidException {

		final ISession session = sessionFactory.createSession(merchantId, serviceId, password);

		Assert.assertEquals(merchantId, session.getMerchantId());
		Assert.assertEquals(serviceId, session.getServiceId());
		Assert.assertNotNull(session.getSessionId());
	}

	@Test
	public void created_session_should_persist() throws RequestNotValidException {

		final Session session = (Session) sessionFactory.createSession(merchantId, serviceId, password);
		verify(sessionRepository).store(any(Session.class));
	}

	


	private void mockSessionRepository() {
		reset(sessionRepository);
		when(sessionRepository.store(any(Session.class))).thenAnswer(new Answer<Session>() {

			@Override
			public Session answer(InvocationOnMock invocation) throws Throwable {
				return (Session) invocation.getArguments()[0];
			}
		});
	}

}
