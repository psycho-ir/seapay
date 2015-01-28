/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 11:16 AM
 */
package com.samenea.seapay.merchant.model;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.IBankAccount;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceTest {

    private Service service;
    private String bankName = "mellat";
    private String mellatAccNo = "ACC-1";

    @Before
    public void before() {
        service = new Service();

    }


    @Test
    public void getAccount_should_return_account_of_given_bank() {
        addBankAccounts();
        IBankAccount bankAccount = service.getAccount(bankName);

        Assert.assertEquals(mellatAccNo, bankAccount.getAccountNumber());
    }

    @Test(expected = NotFoundException.class)
    public void getAccount_should_throw_exception_when_given_bank_does_not_exist(){
        addBankAccounts();
        IBankAccount bankAccount = service.getAccount("NotExistBank");

    }

    private void addBankAccounts() {
        service.addAccount(new BankAccount("ACC-1", "title-1", bankName));
        service.addAccount(new BankAccount("ACC-2", "title-2", "saderat"));
    }
}
