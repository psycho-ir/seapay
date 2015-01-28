

package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionService;
import com.samenea.seapay.transaction.AuthenticationException;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.*;


/**
 * @author: Soroosh Sarabadani
 * Date: 12/30/12
 * Time: 1:17 PM
 */

public class SeapayGatewayServiceTest {

    @InjectMocks
    @Spy
    private SeapayGatewayService seapayGatewayService;


    @Mock
    private ISessionService sessionService;
    @Mock
    private ITransactionService transactionService;

    @Mock
    private IOrderService orderService;

    @Mock
    private ITransaction transaction;
    private final String password = "PASS";
    private final String merchantId = "M-100";
    private final String serviceId = "S-100";
    private final String sessionId = "SESSION-100";
    private final String customerId = "CUSTOMER-100";
    private final String callbackUrl = "http://google.com";
    private final String description = "Description";
    private final int amount = 1000;
    private final String transctionId = "TRN-100";
    private ISession session;

    @Before
    public void before() {

        MockitoAnnotations.initMocks(this);
        when(transaction.getTransactionId()).thenReturn(transctionId);
        when(transaction.getAmount()).thenReturn(amount);
        when(transactionService.getTransaction(transctionId)).thenReturn(transaction);
        setupMocksForSession();
        setupOrder(true);

    }

    @Test
    public void createSession_should_call_createSession_of_sessionFactory() {
        seapayGatewayService.createSession(merchantId, serviceId, password);
        verify(sessionService).createSession(merchantId, serviceId, password);
    }

    @Test
    public void createTransaction_should_find_session() {
        seapayGatewayService.createTransaction(sessionId, amount, description, callbackUrl, customerId);
        verify(sessionService).getSession(sessionId);
    }

    @Test
    public void createTransaction_should_return_the_id_of_createdTransaction() {
        final String result = seapayGatewayService.createTransaction(sessionId, amount, description, callbackUrl, customerId);
        Assert.assertEquals(transctionId, result);
    }

    @Test
    public void commitTransaction_should_find_session() {
        seapayGatewayService.commitTransaction(sessionId, transctionId, amount);
        verify(sessionService, times(2)).getSession(sessionId);
    }

    @Test
    public void commitTransaction_should_call_commit_of_session() {
        seapayGatewayService.commitTransaction(sessionId, transctionId, amount);
        verify(session).commit(transctionId, amount);
    }

    @Test(expected = AuthenticationException.class)
    public void commitTransaction_should_throw_AuthenticationException_if_there_is_no_such_session() {
        when(sessionService.getSession(sessionId)).thenThrow(new NotFoundException(""));
        seapayGatewayService.commitTransaction(sessionId, transctionId, amount);
        verify(transaction).commit();
    }

    @Test(expected = AuthenticationException.class)
    public void commitTransaction_should_throw_AuthenticationException_if_transaction_is_not_for_this_merchant_session() {
        setupOrder(false);
        seapayGatewayService.commitTransaction(sessionId, transctionId, amount);
        verify(transaction).commit();
    }

    @Test(expected = NotFoundException.class)
    public void getTransactionStatus_should_throw_NotFoundException_for_not_existing_transaction() {
        when(transactionService.getTransaction(transctionId)).thenThrow(new NotFoundException(""));
        seapayGatewayService.getTransactionStatus(sessionId, transctionId);
    }

    @Test
    public void getTransactionStatus_should_return_status_of_found_transaction() {
        when(transaction.getStatus()).thenReturn(TransactionStatus.COMMITED);
        when(transactionService.getTransaction(transctionId)).thenReturn(transaction);
        final TransactionStatus transactionStatus = seapayGatewayService.getTransactionStatus(sessionId, transctionId);
        Assert.assertEquals(TransactionStatus.COMMITED, transactionStatus);
    }

    @Test(expected = AuthenticationException.class)
    public void getTransactionStatus_should_throw_authentication_exception_if_is_transaction_is_not_for_this_merchant_session() {
        setupOrder(false);
        final TransactionStatus transactionStatus = seapayGatewayService.getTransactionStatus(sessionId, transctionId);
        Assert.assertEquals(TransactionStatus.COMMITED, transactionStatus);
    }

    @Test(expected = AuthenticationException.class)
    public void getTransactionStatus_should_throw_authentication_exception_if_is_there_is_no_session_with_sessionId() {
        when(sessionService.getSession(sessionId)).thenThrow(new NotFoundException(""));
        final TransactionStatus transactionStatus = seapayGatewayService.getTransactionStatus(sessionId, transctionId);
        Assert.assertEquals(TransactionStatus.COMMITED, transactionStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTransactionStatus_does_not_accept_null_transactionId() {
        seapayGatewayService.getTransactionStatus(sessionId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTransactionStatus_does_not_accept_empty_transactionId() {
        seapayGatewayService.getTransactionStatus(sessionId, "  ");
    }

    private void setupOrder(boolean authenticated) {
        IOrder order = mock(IOrder.class);
        when(orderService.findOrderByTransactionId(transctionId)).thenReturn(order);
        when(order.checkIsFor(transctionId, merchantId, serviceId)).thenReturn(authenticated);
    }

    private void setupMocksForSession() {
        session = mock(ISession.class);
        when(sessionService.getSession(sessionId)).thenReturn(session);
        when(session.getMerchantId()).thenReturn(merchantId);
        when(session.getServiceId()).thenReturn(serviceId);
        //for creation session and transaction
        when(session.createTransaction(amount, description, callbackUrl, customerId)).thenReturn(transaction);
        when(sessionService.createSession(merchantId, serviceId, password)).thenReturn(session);

    }

}
