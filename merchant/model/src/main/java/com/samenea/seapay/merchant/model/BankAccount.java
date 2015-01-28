package com.samenea.seapay.merchant.model;

import javax.persistence.Embeddable;

import com.samenea.commons.component.model.ValueObject;
import com.samenea.seapay.merchant.IBankAccount;

import org.springframework.util.Assert;

/**
 * @author Jalal Ashrafi
 */
// TODO: I (Soroosh) added bankName to this value object. we must confirm this.
@Embeddable
public class BankAccount extends ValueObject implements IBankAccount {
	private final String accountNumber;
	private final String title;
	private final String bankName;

	protected BankAccount() {
		this.title = null;
		this.bankName = null;
		this.accountNumber = null;
	}

	public BankAccount(String accountNumber, String title, String bankName) {
		Assert.hasText(accountNumber);
		this.accountNumber = accountNumber;
		this.title = title;
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getBankName() {
		return bankName;
	}

	@Override
	public boolean equals(Object o) {
		// todo currently equality is based on just account number\
		// may it should be based on all values
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BankAccount that = (BankAccount) o;

		if (!accountNumber.equals(that.accountNumber))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return accountNumber.hashCode();
	}

	@Override
	public String toString() {
		return "BankAccount [accountNumber=" + accountNumber + ", title=" + title + ", bankName=" + bankName + "]";
	}

}
