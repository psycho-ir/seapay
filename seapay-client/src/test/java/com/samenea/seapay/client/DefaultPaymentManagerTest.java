package com.samenea.seapay.client;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.client.impl.CommunicationException;
import com.samenea.seapay.client.ws.TransactionStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 */
@ContextConfiguration(locations = { "classpath:context.xml","classpath:contexts/mock.xml" })
public class DefaultPaymentManagerTest extends AbstractJUnit4SpringContextTests{
    @Qualifier(value = "defaultPaymentManager")
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    PaymentSession paymentSession;
    @Autowired
    OrderService orderService;
    @Autowired
    Environment environment;

    final String customerId = "customerId";
    final int amount = 10;
    final String orderId = "ORDER-1";
    final String transactionId = "TRN-1";
    final Date currentDate = new Date();

    @Value("${merchant.callbackUrl}")
    private String callbackUrl;

    @Before
    public void setup(){
        reset(paymentSession);
        reset(orderService);
        when(paymentSession.getTransactionStatus(transactionId)).thenReturn(TransactionStatus.BANK_RESOLVED);
        when(paymentSession.getBankName(transactionId)).thenReturn("mellat");
        when(orderService.orderAmount(orderId)).thenReturn(amount);
        when(environment.getCurrentDate()).thenReturn(currentDate);
    }

    @Test
    public void submitTransaction_should_return_created_transaction_id(){
        final String webserviceTransactionId = "TRN-1";
        when(paymentSession.createTransaction(amount,"nodesc",callbackUrl+orderId,customerId)).thenReturn(webserviceTransactionId);
        String transactionId = paymentManager.startTransaction(orderId, customerId);
        assertEquals(webserviceTransactionId, transactionId);
    }

    //region processTransaction tests
    @Test
    public void processTransaction_should_call_deliver_service_when_transaction_committed_successfully(){
        paymentManager.processTransaction(orderId, transactionId);
        verify(orderService).deliver(orderId, transactionId,"mellat");
        verify(paymentSession).commitTransaction(transactionId, amount);
    }
    @Test
    public void processTransaction_should_postponeDelivery_when_could_not_commit_transaction(){
        doThrow(new CommunicationException("Mock Communication Exception")).when(paymentSession).commitTransaction(transactionId,amount);
        try {
            paymentManager.processTransaction(orderId, transactionId);
            verify(paymentSession).getTransactionStatus(transactionId);
            fail("We expect Communication exception");
        } catch (CommunicationException e) {
            //OK we expect it
        }
        verify(orderService).postponeDelivery(orderId, transactionId);
        verify(orderService,never()).deliver(orderId, transactionId,"mellat");
    }

    @Test
    public void processTransaction_should_call_deliver_when_transaction_is_already_committed(){
//        todo
        when(paymentSession.getTransactionStatus(transactionId)).thenReturn(TransactionStatus.COMMITED);
        paymentManager.processTransaction(orderId, transactionId);

        verify(orderService).deliver(orderId, transactionId,"mellat");
        verify(paymentSession,never()).commitTransaction(anyString(),anyInt());
    }
    @Test
    public void processTransaction_should_call_cancelOrder_if_transaction_status_is_FAILED_OR_NEW(){
        when(paymentSession.getTransactionStatus(transactionId)).thenReturn(TransactionStatus.FAILED);
        try {
            paymentManager.processTransaction(orderId, transactionId);
            fail("Exception expected ");
        } catch (IllegalStateException e) {
            verify(orderService).cancel(orderId);
            verify(paymentSession,never()).commitTransaction(anyString(),anyInt());
        }
    }
    //endregion
}