package com.samenea.seapay.bank.model;

import com.google.common.collect.ImmutableMap;
import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.*;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.rmi.RemoteException;
import java.util.*;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Jalal Ashrafi
 */
public class BankTransactionServiceTest {
    public static final String CUSTOMER_ID = "10030039";
    private List<Ibank> customerBanks;
    @InjectMocks
    private IBankTransactionService bankTransactionService = new BankTransactionService();
    @Mock
    private BankRepository bankRepository;
    @Mock
    private GatewayFinder gatewayFinder;
    @Mock
    private BankTransactionInfoRepository bankTransactionInfoRepository;

    @Mock
    BankAccountRepository bankAccountRepository;
    @Mock
    private IBankAccount bankAccount;
    @Mock
    private GatewayPlugin gatewayPlugin;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private Environment environment;

    private final String bankName = "mellat";
    private final String accountNumber = "1111";
    private final Integer amount = 1212;
    private Map<String, String> parameters = new HashMap<String, String>();
    private String transactionId = "TRN-100";
    @Mock
    private ITransaction transaction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        when(gatewayFinder.findByName(bankName)).thenReturn(gatewayPlugin);
        mockTransactionInfo();
        customerBanks = new ArrayList<Ibank>();
        customerBanks.add(new Bank("mellat"));
        when(transaction.getLastUpdateDate()).thenReturn(new Date());
        when(environment.getCurrentDate()).thenReturn(new Date());
        parameters = ImmutableMap.of("param1", "value1", "param2", "value2");
    }

    @Test
    public void testGetCustomerBanks() {
        when(bankRepository.getCustomerBanks(CUSTOMER_ID)).thenReturn(customerBanks);
        List<? extends BankInfo> returnedBankInfos = bankTransactionService.customerBanks(CUSTOMER_ID);
        verify(bankRepository, times(1)).getCustomerBanks(CUSTOMER_ID);

        assertEquals(customerBanks.size(), returnedBankInfos.size());
        assertEquals(customerBanks.get(0), returnedBankInfos.get(0));
    }

    @Test
    public void startTransaction_should_call_gatewayFinder_correspond_transaction_bankName() throws GatewayNotFoundException {
        RedirectData redirectData = RedirectData.createForPost("http://google.com", parameters);
        when(gatewayPlugin.request(transaction)).thenReturn(redirectData);
        bankTransactionService.startTransaction(transaction.getTransactionId(), bankAccount);
        verify(gatewayFinder).findByName(bankName);
    }

    @Test(expected = TransactionStartException.class)
    public void startTransaction_should_call_request_of_plugin() throws GatewayNotFoundException {
        RedirectData redirectData = RedirectData.createForPost("http://google.com", parameters);
        when(gatewayPlugin.request(transaction)).thenReturn(redirectData);
        when(gatewayPlugin.request(transaction)).thenThrow(TransactionStartException.class);
        bankTransactionService.startTransaction(transaction.getTransactionId(), bankAccount);

        verify(gatewayPlugin).request(transaction);
    }

    @Test
    public void startTransaction_should_store_bank_transaction_info_in_both_start_and_payment() throws GatewayNotFoundException {
        final RedirectData redirectData = RedirectData.createForPost("http://google.com", parameters);
        when(gatewayPlugin.request(transaction)).thenReturn(redirectData);
        bankTransactionService.startTransaction(transaction.getTransactionId(), bankAccount);

        ArgumentCaptor<BankTransactionInfo> storeArgument = ArgumentCaptor.forClass(BankTransactionInfo.class);
        verify(bankTransactionInfoRepository).store(storeArgument.capture());
        assertEquals(transaction.getBankName(), storeArgument.getValue().getBankName());
        assertEquals(transaction.getTransactionId(), storeArgument.getValue().getTransactionId());
        assertEquals(redirectData.getParameters(), storeArgument.getValue().getTransactionStartParams());
        assertEquals(redirectData.getParameters(), storeArgument.getValue().getPaymentResponseParams());
    }

    @Test
    public void process_response_should_return_PAYMENT_OK_if_gateway_returns_PAYMENT_OK() throws GatewayNotFoundException, RemoteException {
        final PaymentResponseCode gateWayProcessResult = PaymentResponseCode.PAYMENT_OK;
        Map<String, String> responseParameters = ImmutableMap.of("key1", "value1", "key2", "value2");
        when(gatewayPlugin.interpretResponse(transaction, responseParameters)).thenReturn(gateWayProcessResult);
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(transaction.getTransactionId(), transaction.getBankName());
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);

        final PaymentResponseCode result = bankTransactionService.interpretPaymentResponse(transaction, responseParameters);
        Assert.assertEquals(gateWayProcessResult, result);
    }




    @Test(expected = NotFoundException.class)
    public void startTransaction_should_throw_exception_if_transaction_not_found() {
        when(transactionService.getTransaction(anyString())).thenThrow(NotFoundException.class);
        bankTransactionService.startTransaction(transaction.getTransactionId(), bankAccount);
    }

    @Test
    public void startTransaction_should_set_transaction_bank_and_account() {
        IRedirectData redirectData = RedirectData.createForPost("http://google.com", parameters);
        when(gatewayPlugin.request(transaction)).thenReturn(redirectData);

        bankTransactionService.startTransaction(transaction.getTransactionId(), bankAccount);
        verify(transaction).mapToBank(bankAccount);
    }

    //    Interpreter response tests
    @Test(expected = IllegalArgumentException.class)
    public void interpret_response_does_not_accept_null_transaction_info() {
        bankTransactionService.interpretPaymentResponse(null, parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void interpret_response_does_not_accept_null_parameters() {
        bankTransactionService.interpretPaymentResponse(transaction, null);
    }

    @Test
    public void interpret_response_should_return_true_if_plugin_returns_true() {
        testByGateWayInterfaceResult(PaymentResponseCode.PAYMENT_OK);
    }

    @Test
    public void interpret_response_should_return_false_if_plugin_returns_false() {
        testByGateWayInterfaceResult(PaymentResponseCode.CANCELED_BY_USER);
    }

    //parametrized
    private void testByGateWayInterfaceResult(PaymentResponseCode gatewayInterpretResult) {
        when(gatewayPlugin.interpretResponse(transaction, parameters)).thenReturn(gatewayInterpretResult);
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        final PaymentResponseCode interpretResult = bankTransactionService.interpretPaymentResponse(transaction, parameters);
        verify(bankTransactionInfo).savePaymentResponseParams(parameters);
        verify(bankTransactionInfoRepository).store(bankTransactionInfo);
        assertEquals(gatewayInterpretResult, interpretResult);
    }

    //Commit transaction tests
    @Test
    public void commit_transaction_should_return_true_if_gateway_returns_true() {
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        Map<String, String> responseParameters = ImmutableMap.of("key1", "value1", "key2", "value2");
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(responseParameters);
        when(gatewayPlugin.verify(transaction, responseParameters)).thenReturn(true);
        boolean commitResult = bankTransactionService.commitTransaction(transaction);
        assertTrue("Commit result should be true", commitResult);
        verify(gatewayPlugin).verify(transaction, responseParameters);
    }

    @Test
    public void commit_transaction_should_return_false_if_gateway_returns_false() {
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        Map<String, String> responseParameters = ImmutableMap.of("key1", "value1", "key2", "value2");
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(responseParameters);
        when(gatewayPlugin.verify(transaction, responseParameters)).thenReturn(false);
        boolean commitResult = bankTransactionService.commitTransaction(transaction);
        assertFalse("Commit result should be false", commitResult);
        verify(gatewayPlugin).verify(transaction, responseParameters);
    }

    @Test(expected = CommunicationException.class)
    public void commit_transaction_should_throw_CommunicationException_when_plugin_throws_it() {
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        Map<String, String> responseParameters = ImmutableMap.of("key1", "value1", "key2", "value2");
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(responseParameters);
        when(gatewayPlugin.verify(transaction, responseParameters)).thenThrow(new CommunicationException());
        bankTransactionService.commitTransaction(transaction);
    }

    //pollTransaction tests
    @Test
    public void pollTransaction_should_return_true_if_plugin_returns_true() {
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        when(gatewayPlugin.isCommited(eq(transaction),any(BankTransactionInfo.class))).thenReturn(true);
        boolean result = bankTransactionService.pollTransaction(transaction);
        Assert.assertTrue(result);
    }

    @Test
    public void pollTransaction_should_return_false_if_plugin_returns_false() {
        BankTransactionInfo bankTransactionInfo = mock(BankTransactionInfo.class);
        when(bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId())).thenReturn(bankTransactionInfo);
        when(gatewayPlugin.isCommited(eq(transaction), any(BankTransactionInfo.class))).thenReturn(false);
        boolean result = bankTransactionService.pollTransaction(transaction);
        Assert.assertFalse(result);
    }

    private void mockTransactionInfo() {
        when(transaction.getTransactionId()).thenReturn(transactionId);
        when(transaction.getBankName()).thenReturn(bankName);
        when(transaction.getAccountNumber()).thenReturn(accountNumber);
        when(transaction.getAmount()).thenReturn(amount);
    }

}
