package com.samenea.seapay.merchant.service;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/context.xml","classpath:/contexts/mock.xml" })
public abstract class MerchantBaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

}
