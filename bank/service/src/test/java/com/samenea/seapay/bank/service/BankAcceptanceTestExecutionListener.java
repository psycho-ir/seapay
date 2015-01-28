package com.samenea.seapay.bank.service;

import com.samenea.commons.component.utils.test.AbstractAcceptanceTestExecutionListener;

public class BankAcceptanceTestExecutionListener extends AbstractAcceptanceTestExecutionListener {

	@Override
	protected String getDatasourceName() {
		return "dataSource";
	}

	@Override
	protected String getXmlDataFileLoaderName() {
		return "fullXmlDataFileLoader";
	}

}
