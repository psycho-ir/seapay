package com.samenea.seapay.transaction.model;

import junit.framework.Assert;
import org.junit.Test;
import com.samenea.commons.component.model.ValueObject;
import com.samenea.seapay.transaction.model.Account;


public class AccountTest {
	com.samenea.seapay.transaction.IAccount transactionAccount;
	private final String accountNumber = "123";
	private final String bankName="bank";
	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_when_bankName_is_null(){
		transactionAccount = new Account(accountNumber, null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_when_bankName_is_empty(){
		transactionAccount = new Account(accountNumber, "     ");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_when_accountNumber_is_null(){
		transactionAccount = new Account(null, bankName);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_when_accountNumber_is_empty(){
		transactionAccount = new Account("    ", bankName);
	}
	
	@Test
	public void should_create_TransactionAccount_and_set_properties(){
		transactionAccount = new Account(accountNumber, bankName);
		
		Assert.assertEquals(accountNumber, transactionAccount.getAccountNumber());
		Assert.assertEquals(bankName, transactionAccount.getBankName());
	}
	
	@Test
	public void equals_should_return_true_when_two_objects_are_equal(){
		transactionAccount = new Account(accountNumber, bankName);
		com.samenea.seapay.transaction.IAccount newTransactionAccount = new Account(accountNumber, bankName);
		
		Assert.assertTrue(transactionAccount.equals(newTransactionAccount));
	} 
	
	@Test
	public void equals_should_return_false_when_two_objects_are_not_eual(){
		transactionAccount = new Account(accountNumber, bankName);
		com.samenea.seapay.transaction.IAccount newTransactionAccount = new Account("another account number", bankName);
		
		Assert.assertFalse(transactionAccount.equals(newTransactionAccount));
	}
	
	
}
