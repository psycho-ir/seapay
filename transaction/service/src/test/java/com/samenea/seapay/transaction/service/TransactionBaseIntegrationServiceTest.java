package com.samenea.seapay.transaction.service;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/context.xml","classpath:/contexts/integration-mock.xml" })
@TestExecutionListeners({TransactionAcceptanceTestExecutionListener.class})
public abstract class TransactionBaseIntegrationServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

}
