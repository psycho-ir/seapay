package com.samenea.seapay.client.web;

import com.samenea.seapay.client.PaymentManager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 */
@ContextConfiguration(locations = { "classpath:context.xml","classpath:contexts/mock.xml" })
public class ProcessTransactionControllerTest extends AbstractJUnit4SpringContextTests{
    @Autowired
    ProcessTransactionController processTransactionController;
    final String transactionId = "TRN-1";
    final String orderId = "ORDER-1";
    @Autowired
    PaymentManager paymentManager;
    @Before
    public void setup(){
        reset(paymentManager);

    }

    @Test
    public void process_should_call_processTransaction_on_paymentManager_when_SUCCESS_is_reported(){
        final String result = processTransactionController.process(transactionId, orderId,ProcessTransactionController.TRANSACTION_SUCCESS);
        verify(paymentManager).processTransaction(orderId, transactionId);
        assertEquals("SUCCESS", result);
    }
    @Test
    public void process_should_return_call_when_reported_transaction_result_is_not_SUCCESS(){
        final String result = processTransactionController.process(transactionId, orderId,"FAILED");
        verify(paymentManager,never()).processTransaction(orderId, transactionId);
        assertEquals("FAILED", result);
    }
}
