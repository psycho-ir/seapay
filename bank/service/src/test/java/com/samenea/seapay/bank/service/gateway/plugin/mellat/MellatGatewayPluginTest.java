package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.command.MaxRetryReachedException;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.model.*;
import com.samenea.seapay.bank.service.BankAccountService;
import com.samenea.seapay.bank.service.BankBaseServiceTest;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.repository.UnknowTransactionRepository;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import com.samenea.seapay.transaction.TransactionInfo;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MellatGatewayPluginTest extends BankBaseServiceTest {
    private final static String SALE_REFERENCE_ID = "SaleReferenceId";
    private final static String SALE_ORDER_ID = "SaleOrderId";

    private final static Long orderId = 100L;
    private final static Long refId = 123L;

    @InjectMocks
    private MellatGatewayPlugin mellatGatewayPlugin = new MellatGatewayPlugin();

    @Mock
    private Environment environment;
    @Mock
    private MellatWSWrapper mellatWSWrapper;
    @Mock
    private TransactionInfo transactionInfo;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private BankAccount bankAccount;
    @Mock
    private PaymentGatewayConfiguration paymentGatewayConfiguration;
    @Mock
    private UnknowTransactionRepository unknowTransactionRepository;
    @Mock
    private BankTransactionInfoRepository bankTransactionInfoRepository;

    private final String terminalId = "111";
    private String username = "username";
    private String password = "password";


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2012, 2, 1, 23, 24);
        Date d = calendar.getTime();
        when(environment.getCurrentDate()).thenReturn(calendar.getTime());
        when(bankAccountService.findAccount(anyString())).thenReturn(bankAccount);
        when(bankAccount.getPaymentGatewayConfiguration()).thenReturn(paymentGatewayConfiguration);
        when(paymentGatewayConfiguration.get(MellatGatewayConfigurationReader.TERMINAL_ID)).thenReturn(terminalId);
        when(paymentGatewayConfiguration.get(MellatGatewayConfigurationReader.USERNAME)).thenReturn(username);
        when(paymentGatewayConfiguration.get(MellatGatewayConfigurationReader.PASSWORD)).thenReturn(password);
        when(transactionInfo.getAccountNumber()).thenReturn("111");
        when(transactionInfo.getTransactionNumber()).thenReturn(100L);
        when(transactionInfo.getAmount()).thenReturn(200);
        when(transactionInfo.getTransactionId()).thenReturn("TRN-100");
        when(bankTransactionInfoRepository.findByTransactionId(anyString())).thenReturn(new BankTransactionInfo("TRN-100","mellat"));
    }

    @Test
    public void request_should_call_mellatWSWrapper_payRequest_with_suitable_parameters() throws RemoteException {
        when(mellatWSWrapper.payRequest((AuthenticationData) anyObject(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(new MellatWSResponse("0"));
        mellatGatewayPlugin.request(transactionInfo);

        verify(mellatWSWrapper).payRequest(any(AuthenticationData.class), eq(100L), eq(200L), eq("http://localhost:8181/web/transaction/verification/TRN-100"), eq(""));
    }


    @Test
    public void verify_should_call_mellatWSWrapper_verify_with_suitable_parameters() throws RemoteException {

        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId))).thenReturn(new MellatWSResponse("0"));

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString());

        mellatGatewayPlugin.verify(transactionInfo, parameters);

        verify(mellatWSWrapper).commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId));

    }

    @Test
    public void verify_should_call_mellatWSWrapper_verify_until_it_pass_or_threshold_exceed() throws RemoteException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString());

        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class);
        when(mellatWSWrapper.check(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        mellatGatewayPlugin.verify(transactionInfo, parameters);
        verify(mellatWSWrapper, times(3)).check(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId));
    }

    @Test
    public void verify_should_return_false_when_orderId_is_not_same_with_transactionNumber() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString() + "1");
        final boolean verified = mellatGatewayPlugin.verify(transactionInfo, parameters);
        Assert.assertFalse(verified);

    }

    @Test
    public void verify_should_call_mellatWSWrapper_settle_if_verify_complete_successfully() throws RemoteException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString());
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId))).thenReturn(new MellatWSResponse("0"));
        mellatGatewayPlugin.verify(transactionInfo, parameters);

        verify(mellatWSWrapper).commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId));
        verify(mellatWSWrapper).settle(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId));
    }

    @Test
    public void verify_shold_throw_exception_and_persist_transaction_as_unknown() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString());
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId))).thenThrow(CommunicationException.class);
        when(mellatWSWrapper.check(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(refId))).thenThrow(CommunicationException.class);
        boolean exceptionThrowed = false;
        try {
            mellatGatewayPlugin.verify(transactionInfo, parameters);
        } catch (MaxRetryReachedException e) {
            exceptionThrowed = true;
        }

        assertTrue(exceptionThrowed);

        verify(unknowTransactionRepository).store(any(UnknownTransaction.class));

    }

    @Test
    public void interpret_should_return_CANCELED_BY_USER_when_transactionNumber_and_orderId_are_not_same() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString() + "1");
        final PaymentResponseCode responseCode = mellatGatewayPlugin.interpretResponse(transactionInfo, parameters);
        Assert.assertEquals(PaymentResponseCode.CANCELED_BY_USER, responseCode);
    }

    @Test
    public void isCommited_should_return_false_when_transactionNumber_and_orderId_are_not_same(){
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(SALE_REFERENCE_ID, refId.toString());
        parameters.put(SALE_ORDER_ID, orderId.toString() + "1");
        final BankTransactionInfo bankTransactionInfo = new BankTransactionInfo("TRN-100", "mellat");
        bankTransactionInfo.savePaymentResponseParams(parameters);
        final boolean commited = mellatGatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);
        Assert.assertFalse(commited);
    }


    // todo: these tests must complete for parameters validations and error situations

}
