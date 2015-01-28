package com.samenea.seapay.session;

import java.util.Date;

import com.samenea.seapay.transaction.ITransaction;

public interface ISession {

	public String getSessionId();

	public String getMerchantId();

	public String getServiceId();

	public Date getCreateDate();

	ITransaction createTransaction(int amount, String description, String callbackUrl, String customerId);

	void commit(String transactionId,int amount);

	
}
