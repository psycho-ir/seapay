package com.samenea.seapay.transaction.model.session;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.merchant.IMerchant;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.model.Session;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.model.ITransactionFactory;
import com.samenea.seapay.transaction.model.Transaction;
import com.samenea.seapay.transaction.model.TransactionBaseModelTest;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;

public class DefaultSessionTest extends TransactionBaseModelTest {
    @Autowired
    private Environment environment;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private IBankTransactionService bankTransactionService;
    @Autowired
    private ITransactionFactory transactionFactory;
    private IMerchant merchant;

    private Session session;
    private final String merchantId = "M-100";
    private final String sessionId = "S-100";
    private final String serviceId = "srv-100";
    private final String callbackUrl = "http://abcd.com";
    private final String transactionId = "TRN-111";
    private final int amount = 900;
    private final String customerId = "C-100";
    private final String description = "Description";
    private Transaction transaction;
    private IOrder order;


    @Before
    public void before() {
        merchant = mock(IMerchant.class);
        order = mock(IOrder.class);

        reset(transactionService);
        when(merchantService.getMerchant(merchantId)).thenReturn(merchant);
        transaction = new Transaction(transactionId, amount, description);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
    }

    @Test
    public void should_create_session_and_set_its_parameters() {
        Date date = mockDate();
        session = new Session(merchantId, sessionId, serviceId);

        Assert.assertEquals(merchantId, session.getMerchantId());
        Assert.assertEquals(sessionId, session.getSessionId());
        Assert.assertEquals(serviceId, session.getServiceId());
        Assert.assertEquals(date, session.getCreateDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_merchantId_is_null() {
        session = new Session(null, sessionId, serviceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_merchantId_is_empty() {
        session = new Session("    ", sessionId, serviceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_serviceId_is_null() {
        session = new Session(merchantId, sessionId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_serviceId_is_empty() {
        session = new Session(merchantId, sessionId, "        ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_sessionId_is_null() {
        session = new Session(merchantId, null, serviceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_in_constructor_when_sessionId_is_empty() {
        session = new Session(merchantId, "    ", serviceId);
    }

    @Test
    public void session_should_create_invoice_for_created_transaction() throws RequestNotValidException {
        when(transactionFactory.createTransaction(amount, description)).thenReturn(transaction);
        session = new Session(merchantId, sessionId, serviceId);
        when(merchant.createOrder(eq(serviceId), anyString(), eq(callbackUrl), eq(customerId))).thenReturn(order);
        when(order.toString()).thenReturn("Order String");
        session.createTransaction(amount, description, callbackUrl, customerId);
        verify(merchantService).getMerchant(merchantId);
        verify(merchant).createOrder(eq(serviceId), anyString(), eq(callbackUrl), eq(customerId));

    }

    @Test
    public void commit_should_call_getByTransactionId_of_transactionService() {
        transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(amount);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        session = new Session(merchantId, sessionId, serviceId);
        session.commit(transactionId, amount);

        verify(transactionService).getTransaction(transactionId);
    }

    @Test
    public void commit_should_call_commit_of_transaction() {
        transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(amount);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        doNothing().when(transaction).commit();
        session = new Session(merchantId, sessionId, serviceId);
        session.commit(transactionId, amount);

        verify(transaction).commit();
    }

    @Test
    public void commit_should_not_call_bankService_commitTransaction_if_transaction_commit_throw_exception() {
        transaction = mock(Transaction.class);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        doThrow(IllegalStateException.class).when(transaction).commit();
        session = new Session(merchantId, sessionId, serviceId);
        try {
            session.commit(transactionId, amount);
        } catch (Exception e) {
        }

        verify(bankTransactionService, times(0)).commitTransaction(transaction);
    }

    @Test(expected = RequestNotValidException.class)
    public void commit_should_throw_exception_when_amount_is_not_equal() {
        transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(amount + 100);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        doThrow(IllegalStateException.class).when(transaction).commit();
        session = new Session(merchantId, sessionId, serviceId);

        session.commit(transactionId, amount);
    }


    private Date mockDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 2, 2);
        when(environment.getCurrentDate()).thenReturn(calendar.getTime());
        return calendar.getTime();
    }

}
