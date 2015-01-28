package com.samenea.seapay.bank.service;

import com.samenea.seapay.bank.model.BankAccount;

public interface BankAccountService {
	BankAccount findAccount(String accountNumber);
}
