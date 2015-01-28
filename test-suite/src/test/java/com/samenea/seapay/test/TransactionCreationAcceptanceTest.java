package com.samenea.seapay.test;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.transaction.ISeapayGatewayService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.annotation.DataSets;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/2/13
 * Time: 2:30 PM
 */

public class TransactionCreationAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private ISeapayGatewayService seapayGatewayService;

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IOrderService orderService;

    private String sessionId = "7323df46-cf87-4560-be3b-39ec3d2b9047";
    private int amount = 1000;
    private String description = "DESC";
    private String callbackUrl = "http://test.com";
    private String customerId = "customer-1";


    @Test
    @Rollback(false)
    @DataSets(setUpDataSet = "/sample-data/TransactionCreationAcceptanceTest.xml")
    public void should_create_transaction_and_persist_it() {
        final String result = seapayGatewayService.createTransaction(sessionId, amount, description, callbackUrl, customerId);
        ITransaction transaction = transactionService.getTransaction(result);
        IOrder order = orderService.findOrderByTransactionId(result);

        Assert.assertNotNull(transaction);
        Assert.assertNotNull(order);
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(description, transaction.getDescription());
        Assert.assertEquals(callbackUrl, order.getCallbackUrl());
        Assert.assertEquals(customerId,order.getCustomerId());
        Assert.assertEquals(TransactionStatus.NEW,transaction.getStatus());
    }

    @Test(expected = NotFoundException.class)
    @DataSets(setUpDataSet = "/sample-data/TransactionCreationAcceptanceTest.xml")
    public void should_throw_exception_if_session_doest_not_exist() {
        final String result = seapayGatewayService.createTransaction("SSS", amount, description, callbackUrl, customerId);
    }

}
