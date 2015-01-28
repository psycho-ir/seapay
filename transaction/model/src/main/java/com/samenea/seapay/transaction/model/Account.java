package com.samenea.seapay.transaction.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

import com.samenea.commons.component.model.ValueObject;
import com.samenea.seapay.transaction.IAccount;

/**
 * Value object of Account that we use in our transactions. this class create a
 * snapshot from accountNumber. because it can be changed, but in our
 * transaction we need to track actual AccountNumber.
 * 
 * @author soroosh
 * 
 */
@Embeddable
public class Account extends ValueObject implements IAccount {
	@Column(nullable = false)
	private final String accountNumber;
	@Column(nullable = false)
	private final String bankName;

	private Account() {
		this.accountNumber = null;
		this.bankName = null;
	}

	public Account(String accountNumber, String bankName) {
		Assert.notNull(accountNumber, "Account Number cannot be null.");
		Assert.notNull(bankName, "Bank Name cannot be null.");
		Assert.hasText(accountNumber, "Account Number cannot be empty");
		Assert.hasText(bankName, "Bank Name cannot be empty.");
		this.accountNumber = accountNumber;
		this.bankName = bankName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.samenea.seapay.transaction.model.acc#getAccountNumber()
	 */
	@Override
	public String getAccountNumber() {
		return this.accountNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.samenea.seapay.transaction.model.acc#getBankName()
	 */
	@Override
	public String getBankName() {
		return this.bankName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransactionAccount [accoutNumber=" + accountNumber + ", bankName=" + bankName + "]";
	}

}
