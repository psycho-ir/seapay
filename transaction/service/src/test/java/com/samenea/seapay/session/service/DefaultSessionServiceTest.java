package com.samenea.seapay.session.service;

import com.samenea.seapay.session.ISessionService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.model.Session;
import com.samenea.seapay.session.model.SessionRepository;
import com.samenea.seapay.transaction.service.TransactionBaseServiceTest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultSessionServiceTest extends TransactionBaseServiceTest {
	@Autowired
    ISessionService sessionService;
	@Autowired
	IMerchantService merchantService;
	@Autowired
	SessionRepository sessionRepository;
	String merchantId = "M-100";
	String serviceId = "S-100";
	String password = "P-100";

	@Test
	public void should_create_session() throws RequestNotValidException {
		when(merchantService.isMerchantValid(merchantId, serviceId, password)).thenReturn(true);
		mockSessionRepository();
		ISession session = sessionService.createSession(merchantId, serviceId, password);
		Assert.assertNotNull(session);
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
