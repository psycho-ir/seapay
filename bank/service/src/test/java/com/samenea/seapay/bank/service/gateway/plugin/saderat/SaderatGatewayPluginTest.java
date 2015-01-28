package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.model.*;
import com.samenea.seapay.bank.service.BankAccountService;
import com.samenea.seapay.transaction.TransactionInfo;
import com.samenea.seapay.transaction.TransactionStatus;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 1:19 PM
 */

public class SaderatGatewayPluginTest {
    private final String transaction_key = "123";
    private final String trans_id = "100000";
    private final String response_code = "1";
    private final String reason_code = "100";
    private final String sub_code = "0";
    private final String sequence = "100";
    private final String time_stamp = "12334432";
    private final String amount = "10000";
    private final String currency = "Rial";
    private final String reason_text = "reason_text";
    private final String login = "login";
    private Logger logger = LoggerFactory.getLogger(SaderatGatewayPluginTest.class);

    @InjectMocks
    @Spy
    private SaderatGatewayPlugin gatewayPlugin;

    private Map<String, String> parameters = new HashMap<String, String>();
    @Mock
    private TransactionInfo transactionInfo;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private SaderatGatewayConnectionUtil saderatGatewayConnectionUtil;
    @Mock
    private BankTransactionInfo bankTransactionInfo;
    @Mock
    private BankTransactionInfoRepository bankTransactionInfoRepository;
    @Mock
    private Environment environment;
    private static Date currentDate = new Date();
    List<String> codesForHash;


    @Before
    public void before() throws InvalidKeyException, NoSuchAlgorithmException {
        MockitoAnnotations.initMocks(this);
        when(environment.getCurrentDate()).thenReturn(currentDate);
        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.BANK_RESOLVED);

        codesForHash = new ArrayList<String>();
        parameters.put("x_trans_id", trans_id);
        parameters.put("x_response_code", response_code);
        parameters.put("x_response_subcode", sub_code);
        parameters.put("x_response_reason_code", reason_code);
        parameters.put("x_response_reason_text", reason_text);
        parameters.put("x_login", login);
        parameters.put("x_fp_sequence", sequence);
        parameters.put("x_fp_timestamp", time_stamp);
        parameters.put("x_amount", amount);
        parameters.put("x_currency_code", currency);
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash) + "1");

        codesForHash.add(trans_id);
        codesForHash.add(response_code);
        codesForHash.add(sub_code);
        codesForHash.add(reason_code);
        codesForHash.add(reason_text);
        codesForHash.add(login);
        codesForHash.add(sequence);
        codesForHash.add(time_stamp);
        codesForHash.add(amount);
        codesForHash.add(currency);

    }


    @Test
    public void interpret_should_return_paymentResponse_CANCELED_BY_USER_if_hash_code_is_not_ok() {
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        PaymentResponseCode responseCode = gatewayPlugin.interpretResponse(transactionInfo, parameters);
        Assert.assertEquals(PaymentResponseCode.CANCELED_BY_USER, responseCode);
    }

    @Test
    public void interpret_should_return_paymentResponse_PAYMENT_OK_if_hash_code_is_ok_and_response_code_is_1() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        PaymentResponseCode responseCode = gatewayPlugin.interpretResponse(transactionInfo, parameters);
        Assert.assertEquals(PaymentResponseCode.PAYMENT_OK, responseCode);
    }

    @Test
    public void interpret_should_return_paymentResponse_CANCELED_BY_USER_if_hash_code_is_ok_and_response_code_is_not_1() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_response_code", "2");
        codesForHash.set(1, "2");
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        PaymentResponseCode responseCode = gatewayPlugin.interpretResponse(transactionInfo, parameters);
        Assert.assertEquals(PaymentResponseCode.CANCELED_BY_USER, responseCode);
    }

    @Test
    public void interpret_should_return_paymentResponse_PAYMENT_OK_if_transactionNumber_is_not_the_same_of_response() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence + "1"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        PaymentResponseCode responseCode = gatewayPlugin.interpretResponse(transactionInfo, parameters);
        Assert.assertEquals(PaymentResponseCode.CANCELED_BY_USER, responseCode);
    }

    @Test
    public void interpret_should_read_payment_response_when_transaction_is_UNKNOWN_and_return_result_with_interpreting_this() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence));
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(parameters);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);

        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.UNKNOWN);
//        parameters.put("x_response_code", "2");
        PaymentResponseCode responseCode = gatewayPlugin.interpretResponse(transactionInfo, parameters);

        Assert.assertEquals(PaymentResponseCode.PAYMENT_OK, responseCode);
    }

    @Test
    public void verify_should_return_true() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence));
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(parameters);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);

        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.UNKNOWN);
        final boolean verify = gatewayPlugin.verify(transactionInfo, parameters);

        Assert.assertTrue(verify);
    }

    @Test(expected = IllegalStateException.class)
    public void verify_should_throw_exception_when_response_is_null() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence));
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(null);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);

        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.UNKNOWN);
        final boolean verify = gatewayPlugin.verify(transactionInfo, parameters);

    }

    @Test
    public void verify_should_return_false_when_transactionId_is_not_the_same() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash));
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        when(transactionInfo.getTransactionNumber()).thenReturn(Long.valueOf(sequence + "1"));
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(parameters);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);


        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.UNKNOWN);
        final boolean verify = gatewayPlugin.verify(transactionInfo, parameters);

        Assert.assertFalse(verify);
    }

    @Test
    public void verify_should_return_false_when_hash_is_not_compatible() throws InvalidKeyException, NoSuchAlgorithmException {
        parameters.put("x_fp_hash", new SaderatHashUtil().hash(transaction_key, codesForHash) + "1");
        BankAccount account = new BankAccount();
        account.setPaymentGatewayConfiguration(new PaymentGatewayConfiguration("x_login!222959300013;x_transactionkey!123;"));
        when(bankAccountService.findAccount(anyString())).thenReturn(account);
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfo.getPaymentResponseParams()).thenReturn(parameters);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);

        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.UNKNOWN);
        final boolean verify = gatewayPlugin.verify(transactionInfo, parameters);

        Assert.assertFalse(verify);
    }

    @Test
    public void isCommited_should_return_true_if_the_result_of_bank_response_code_is_1() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);
        when(saderatGatewayConnectionUtil.request(anyString(), any(Map.class))).thenReturn("x_response_code=1");
        when(bankTransactionInfo.getTransactionStartParams()).thenReturn(parameters);
        Boolean result = gatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);

        Assert.assertTrue(result);

    }

    @Test(expected = SamenRuntimeException.class)
    public void isCommited_should_throw_exception_when_result_is_pending() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);
        when(saderatGatewayConnectionUtil.request(anyString(), any(Map.class))).thenReturn("x_response_code=4");
        when(transactionInfo.getLastUpdateDate()).thenReturn(new Date());
        when(bankTransactionInfo.getTransactionStartParams()).thenReturn(parameters);
        gatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);


    }

    @Test
    public void isCommited_should_return_false_when_result_is_pending_and_lastUpdateDate_is_more_than_expected() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        when(transactionInfo.getTransactionId()).thenReturn(trans_id);
        when(bankTransactionInfoRepository.findByTransactionId(trans_id)).thenReturn(bankTransactionInfo);
        when(saderatGatewayConnectionUtil.request(anyString(), any(Map.class))).thenReturn("x_response_code=4");
        final Date lastUpdate = new Date(environment.getCurrentDate().getTime() - 60 * 1000 * 30);

        when(transactionInfo.getLastUpdateDate()).thenReturn(lastUpdate);
        when(bankTransactionInfo.getTransactionStartParams()).thenReturn(parameters);
        final boolean commited = gatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);

        Assert.assertFalse(commited);
    }

    @Test
    public void isCommited_should_return_false_if_parameters_is_null() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        when(saderatGatewayConnectionUtil.request(anyString(), any(Map.class))).thenReturn("x_response_code=1");
        when(bankTransactionInfo.getTransactionStartParams()).thenReturn(null);
        final boolean commited = gatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);
        Assert.assertFalse(commited);
    }

    @Test
    public void isCommited_should_return_false_if_parameters_is_empty() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        when(saderatGatewayConnectionUtil.request(anyString(), any(Map.class))).thenReturn("x_response_code=1");
        when(bankTransactionInfo.getTransactionStartParams()).thenReturn(new HashMap<String, String>());
        final boolean commited = gatewayPlugin.isCommited(transactionInfo, bankTransactionInfo);
        Assert.assertFalse(commited);
    }


}
