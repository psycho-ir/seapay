package com.samenea.seapay.transaction.service;

import com.samenea.seapay.transaction.ITransactionService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/context.xml","classpath:/contexts/mock.xml" })
public abstract class TransactionBaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

}
