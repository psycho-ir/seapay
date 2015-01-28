/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 11:01 AM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;


public class MellatResponseParamsTest {

    private HashMap<String, String> params;
    private final static String SALE_REFERENCE_ID = "SaleReferenceId";
    private final static String SALE_ORDER_ID = "SaleOrderId";
    private final static String ACCOUNT_NUMBER = "accountNumber";
    private final static Long refId = 123L;
    private final static Long orderId = 222L;
    private final static String accNo = "ACC-100";


    @Before
    public void before() {
        params = new HashMap<String, String>();
        params.put(SALE_REFERENCE_ID, refId.toString());
        params.put(SALE_ORDER_ID, orderId.toString());
        params.put(ACCOUNT_NUMBER, accNo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cosnturctor_should_throw_exception_if_parameters_is_null() {
        MellatResponseParams reader = new MellatResponseParams(null);
    }

    @Test(expected = SamenRuntimeException.class)
    public void constructor_should_throw_exception_if_parameters_is_not_compatible() {

        params.clear();
        new MellatResponseParams(params);
    }

    @Test
    public void should_return_saleReferenceId() {
        MellatResponseParams reader = new MellatResponseParams(params);

        Assert.assertEquals(refId, reader.getSaleReferenceId());
    }

//    @Test
//    public void should_return_accountNumber() {
//        MellatParameterReader reader = new MellatParameterReader(params);
//
//        Assert.assertEquals(accNo, reader.getAccountNumber());
//    }

    @Test
    public void should_return_saleOrderId() {
        MellatResponseParams reader = new MellatResponseParams(params);

        Assert.assertEquals(orderId, reader.getSaleOrderId());
    }

    @Test
    public void should_return_0_when_saleRefId_parameter_is_empty() {
        params.put(SALE_REFERENCE_ID, "");
        MellatResponseParams reader = new MellatResponseParams(params);

        Assert.assertEquals(Long.valueOf(0), reader.getSaleReferenceId());
    }

}
