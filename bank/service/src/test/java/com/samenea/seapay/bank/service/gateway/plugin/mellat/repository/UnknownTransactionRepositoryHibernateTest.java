package com.samenea.seapay.bank.service.gateway.plugin.mellat.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.service.BankBaseServiceTest;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransactionStatus;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.annotation.DataSets;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/14/13
 * Time: 9:41 AM
 */

public class UnknownTransactionRepositoryHibernateTest extends BankBaseServiceTest {
    @Autowired
    private UnknowTransactionRepository repositoryHibernate;
    private String transactionId = "TRN-100";

    private static final Date date = Calendar.getInstance().getTime();
    @Autowired
    private Environment environment;

    @Before
    public void before() {
        when(environment.getCurrentDate()).thenReturn(date);
    }

    @Test(expected = NotFoundException.class)
    @DataSets(setUpDataSet = "/test-data/default.xml")
    public void should_throw_exception_when_transaction_does_not_exist() {
        UnknownTransaction byTransactionId = repositoryHibernate.findByTransactionId(transactionId);

    }

    @Test
    @DataSets(setUpDataSet = "/test-data/UnknowTransactionRepositoryHibernateTest.xml")
    public void should_return_unknowtransaction_coresspond_to_transactionId() {
        UnknownTransaction byTransactionId = repositoryHibernate.findByTransactionId(transactionId);
        assertNotNull(byTransactionId);
        assertEquals(transactionId, byTransactionId.getTransactionId());
        assertEquals(UnknownTransactionStatus.SETTLE_PROBLEM, byTransactionId.getStatus());
    }

    @Test
    @DataSets(setUpDataSet = "/test-data/UnknowTransactionRepositoryHibernateTest.xml")
    public void should_return_all_transaction_with_status_SETTLE_PROBLEM() {
         List<UnknownTransaction> transactionList = repositoryHibernate.findByStatus(UnknownTransactionStatus.SETTLE_PROBLEM);
        Assert.assertEquals(1,transactionList.size());
    }

}
