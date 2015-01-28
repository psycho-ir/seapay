package com.samenea.seapay.transaction.repository;

import com.samenea.commons.component.utils.test.AbstractAcceptanceTestExecutionListener;

public class TransactionAcceptanceTestExecutionListener extends AbstractAcceptanceTestExecutionListener {

	@Override
	protected String getDatasourceName() {
		return "dataSource";
	}

	@Override
	protected String getXmlDataFileLoaderName() {
		return "fullXmlDataFileLoader";
	}

}
