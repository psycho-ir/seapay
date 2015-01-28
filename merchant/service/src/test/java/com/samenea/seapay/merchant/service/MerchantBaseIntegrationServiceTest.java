package com.samenea.seapay.merchant.service;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/context.xml" })
@TestExecutionListeners({MerchantAcceptanceTestExecutionListener.class})
public abstract class MerchantBaseIntegrationServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

}
