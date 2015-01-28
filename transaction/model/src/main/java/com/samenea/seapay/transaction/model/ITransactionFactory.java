package com.samenea.seapay.transaction.model;

import com.samenea.seapay.transaction.ITransaction;

public interface ITransactionFactory {

	ITransaction createTransaction(int amount, String description);

}