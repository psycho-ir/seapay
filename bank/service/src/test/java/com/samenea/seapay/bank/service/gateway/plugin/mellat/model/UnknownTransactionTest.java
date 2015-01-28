package com.samenea.seapay.bank.service.gateway.plugin.mellat.model;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.model.BankTransactionInfoRepository;
import com.samenea.seapay.bank.service.BankBaseServiceTest;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/8/13
 * Time: 5:40 PM
 */

public class UnknownTransactionTest extends BankBaseServiceTest {
    private final String transactionId = "TRN-100";

    private UnknownTransaction unknownTransaction;
    @Autowired(required = true)
    private Environment environment;
    @Autowired
    private BankTransactionInfoRepository bankTransactionInfoRepository;

    private static final Date date = Calendar.getInstance().getTime();
    private String accountNumber = "ACC-100";

    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        when(environment.getCurrentDate()).thenReturn(date);

    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_transactionId_is_null() {
        new UnknownTransaction(null, accountNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_transactionId_is_empty() {
        new UnknownTransaction("", accountNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_accountNumber_is_null() {
        new UnknownTransaction(transactionId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_accountNumber_is_empty() {
        new UnknownTransaction(transactionId, "");
    }

    @Test
    public void constructor_should_return_transaction_with_appropriate_status() {
        UnknownTransaction transaction = new UnknownTransaction(transactionId, accountNumber);
        Assert.assertEquals(UnknownTransactionStatus.SETTLE_PROBLEM, transaction.getStatus());
        Assert.assertEquals(date.toString(), transaction.getCreateDate().toString());
        Assert.assertEquals(transactionId, transaction.getTransactionId());
    }

    @Test
    public void equals_should_return_false_when_other_is_null() {
        UnknownTransaction transaction = new UnknownTransaction(transactionId, accountNumber);
        assertFalse(transaction.equals(null));
    }

    @Test
    public void equals_should_return_false_when_transactionId_is_different() {
        UnknownTransaction transaction = new UnknownTransaction(transactionId, accountNumber);
        UnknownTransaction otherTansaction = new UnknownTransaction(transactionId + "2", accountNumber);
        assertFalse(transaction.equals(otherTansaction));
    }

    @Test
    public void equals_should_return_true_with_same_object() {
        UnknownTransaction transaction = new UnknownTransaction(transactionId, accountNumber);
        assertTrue(transaction.equals(transaction));
    }

    @Test
    public void equals_should_return_true_when_transactionId_is_equal() {
        UnknownTransaction transaction = new UnknownTransaction(transactionId, accountNumber);
        UnknownTransaction otherTransaction = new UnknownTransaction(transactionId, accountNumber);
        assertTrue(transaction.equals(otherTransaction));

    }

//    @Test(expected = IllegalStateException.class)
//    public void investigate_should_not_call_when_status_is_SOLVED() {
//        UnknownTransaction transaction = new UnknownTransaction(accountNumber, transactionId);
//
//        transaction.investigate();
//        transaction.investigate();
//
//    }


}

