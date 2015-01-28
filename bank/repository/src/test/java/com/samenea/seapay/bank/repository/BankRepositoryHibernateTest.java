package com.samenea.seapay.bank.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.samenea.seapay.bank.model.BankRepository;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = "classpath*:/context.xml")
public class BankRepositoryHibernateTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private BankRepository bankRepository;

	@Test
	public void t() {
	}
}
