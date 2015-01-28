package com.samenea.seapay.client;

import com.samenea.seapay.client.impl.DefaultPaymentSession;
import com.samenea.seapay.client.ws.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 */
@ContextConfiguration(locations = { "classpath:context.xml","classpath:contexts/mock.xml" })
public class DefaultPaymentSessionTest extends AbstractJUnit4SpringContextTests{
    final String paymentSessionId = "11111-11111";
    final String merchantId = "merchantId";
    final String serviceId = "serviceId";
    final String merchantPassword = "merchantPassword";
    @Autowired
    SeapayGatewayWebService seapayGatewayWebService;
    //todo validation tests
    @Before
    public void setup(){
        when(seapayGatewayWebService.createSession(merchantId, serviceId, merchantPassword)).thenReturn(paymentSessionId);
    }
    @Test
    public void create_transaction_should_return_session_web_service_created_transaction(){
        final DefaultPaymentSession paymentSession = new DefaultPaymentSession(merchantId, serviceId, merchantPassword);
        final int amount = 10;
        final String desc = "desc";
        final String callbackUrl = "callbackUrl";
        final String customerId = "customerId";
        final String transactionId = "TRN-1";
        when(seapayGatewayWebService.createTransaction(paymentSessionId, amount, desc, callbackUrl, customerId)).thenReturn(transactionId);
        final String createdTransactionId = paymentSession.createTransaction(amount, desc, callbackUrl, customerId);
        Assert.assertEquals(transactionId, createdTransactionId);
    }
    @Test
    public void commitTransaction_should_call_commit_transaction_on_webservice() throws AuthenticationException_Exception, IllegalStateException_Exception, CommunicationException_Exception, NotFoundException_Exception {
        final int amount = 10;
        final String transactionId = "TRN-100";
        final DefaultPaymentSession paymentSession = new DefaultPaymentSession(merchantId, serviceId, merchantPassword);
        paymentSession.commitTransaction(transactionId,amount);
        verify(seapayGatewayWebService).commitTransaction(paymentSessionId, transactionId, amount);
    }
    @Test
    public void getTransactionStatus_should_return_webservice_getTransactionStatus_result() throws NotFoundException_Exception, AuthenticationException_Exception {
        final String transactionId = "TRN-100";
        when(seapayGatewayWebService.getTransactionStatus(paymentSessionId,transactionId)).thenReturn(TransactionStatus.BANK_RESOLVED);
        final DefaultPaymentSession paymentSession = new DefaultPaymentSession(merchantId, serviceId, merchantPassword);
        Assert.assertEquals(TransactionStatus.BANK_RESOLVED,paymentSession.getTransactionStatus(transactionId));

    }
}
