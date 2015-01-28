package com.samenea.seapay.bank.model;

/**
 * @author Jalal Ashrafi
 */
public interface BankAccountRepository {
    BankAccount getBankAccountByAccountNumber(String accountNumber);
}
