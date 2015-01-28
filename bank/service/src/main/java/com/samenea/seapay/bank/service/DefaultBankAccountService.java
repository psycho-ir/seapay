package com.samenea.seapay.bank.service;

import com.samenea.seapay.bank.model.BankAccount;
import com.samenea.seapay.bank.model.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 11/14/12
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DefaultBankAccountService implements BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Override
    public BankAccount findAccount(String accountNumber) {
        return bankAccountRepository.getBankAccountByAccountNumber(accountNumber);
    }
}
