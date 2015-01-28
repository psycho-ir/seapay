package com.samenea.seapay.merchant.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@TestExecutionListeners({MerchantAcceptanceTestExecutionListener.class})
@ContextConfiguration(locations = { "classpath:/context.xml","classpath:/contexts/mock.xml" })
public abstract class MerchantBaseRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	@Qualifier("dataSource")
	DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

}
