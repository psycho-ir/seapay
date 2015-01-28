package com.samenea.seapay.bank.model;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * @author Jalal Ashrafi
 */
public class BankTransactionInfoTest {

    public static final String PLUGIN_NAME = "plugin_name";
    public static final String TRANSACTION_ID = "transactionId";
    private Map<String ,String> parameters;
    @Before
    public void setup(){
        parameters = new HashMap<String, String>();
        parameters.put("key1","value1");
        parameters.put("key2","value2");
    }

    @Test
    public void constructor_should_set_fields_correctly(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        assertEquals(PLUGIN_NAME, bankTransactionInfo.getBankName());
        assertEquals(TRANSACTION_ID, bankTransactionInfo.getTransactionId());
        assertTrue(bankTransactionInfo.getPaymentResponseParams().isEmpty());
    }
    @Test(expected = IllegalArgumentException.class)
    public void constructor_does_not_accept_null_transactionId(){
        new BankTransactionInfo(null, PLUGIN_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_does_not_accept_empty_transactionId(){
        new BankTransactionInfo(" ", PLUGIN_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_does_not_accept_null_plugin_name(){
        new BankTransactionInfo(TRANSACTION_ID, null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void constructor_does_not_accept_empty_plugin_name(){
        new BankTransactionInfo(TRANSACTION_ID, " ");
    }

    @Test
    public void testConstructor(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        assertEquals(PLUGIN_NAME, bankTransactionInfo.getBankName());
        assertEquals(TRANSACTION_ID, bankTransactionInfo.getTransactionId());
        assertNotNull(bankTransactionInfo.getPaymentResponseParams());
        assertNotNull(bankTransactionInfo.getTransactionStartParams());
        assertTrue(bankTransactionInfo.getPaymentResponseParams().isEmpty());
        assertTrue(bankTransactionInfo.getTransactionStartParams().isEmpty());
    }
    @Test
    public void save_payment_response_params_should_set_params_correctly(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.savePaymentResponseParams(parameters);
        final Map<String, String> requestParameters = bankTransactionInfo.getPaymentResponseParams();
        assertNotNull(requestParameters);
        assertEquals(parameters, requestParameters);
    }
    @Test
    public void changing_passed_params_to_save_payment_response_params_should_have_no_side_effect(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.savePaymentResponseParams(parameters);
        final Map<String, String> before = Maps.newHashMap(bankTransactionInfo.getPaymentResponseParams());
        parameters.put("newKey","newValue");
        final Map<String, String> after = bankTransactionInfo.getPaymentResponseParams();
        assertEquals(before,after);
    }
    @Test(expected = UnsupportedOperationException.class)
    public void getResponseParams_returned_map_should_be_immutable(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.savePaymentResponseParams(parameters);
        bankTransactionInfo.getPaymentResponseParams().put("new", "new");
    }

    //duplicate tests should be refactored!

    @Test
    public void saveTransactionStartParams_should_set_params_correctly(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.saveTransactionStartParams(parameters);
        final Map<String, String> transactionStartParams = bankTransactionInfo.getTransactionStartParams();
        assertNotNull(transactionStartParams);
        assertEquals(parameters, transactionStartParams);
    }
    @Test
    public void changing_passed_params_to_saveTransactionStartParams_should_have_no_side_effect(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.saveTransactionStartParams(parameters);
        final Map<String, String> before = Maps.newHashMap(bankTransactionInfo.getTransactionStartParams());
        parameters.put("newKey","newValue");
        final Map<String, String> after = bankTransactionInfo.getTransactionStartParams();
        assertEquals(before,after);
    }
    @Test(expected = UnsupportedOperationException.class)
    public void getTransactionStartParams_returned_map_should_be_immutable(){
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(TRANSACTION_ID, PLUGIN_NAME);
        bankTransactionInfo.saveTransactionStartParams(parameters);
        bankTransactionInfo.getTransactionStartParams().put("new","new");
    }
}
