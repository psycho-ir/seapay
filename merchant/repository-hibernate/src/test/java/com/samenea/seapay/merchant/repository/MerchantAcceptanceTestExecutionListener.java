package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.utils.test.AbstractAcceptanceTestExecutionListener;

public class MerchantAcceptanceTestExecutionListener extends AbstractAcceptanceTestExecutionListener {

	@Override
	protected String getDatasourceName() {
		return "dataSource";
	}

	@Override
	protected String getXmlDataFileLoaderName() {
		return "fullXmlDataFileLoader";
	}

}
