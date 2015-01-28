package com.samenea.seapay.merchant.model;

import junit.framework.Assert;
import org.junit.Test;

public class OrderTest {

    private final String merchantId = "M-100";
    private final String password = "password";
    private Order order;
    private String orderId = "ORDER-100";
    private String serviceName = "S-100";
    private Merchant merchant = new Merchant(merchantId, password);
    private String callbackUrl = "http://abc.com";
    private String transactionId = "TRN-100";
    private String customerId = "CUSTOMER-100";

    @Test
    public void should_create_order_and_set_its_properties() {
        order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, customerId);
        Assert.assertEquals(serviceName, order.getServiceName());
        Assert.assertEquals(merchant, order.getMerchant());
        Assert.assertEquals(callbackUrl, order.getCallbackUrl());
        Assert.assertEquals(transactionId, order.getTransactionId());
        Assert.assertEquals(customerId, order.getCustomerId());

    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_orderId_is_empty() {
        order = new Order(null, serviceName, merchant, callbackUrl, transactionId, customerId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_orderId_is_null() {
        order = new Order("    ", serviceName, merchant, callbackUrl, transactionId, customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_service_is_null() {
        order = new Order(orderId, null, merchant, callbackUrl, transactionId, customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_merchant_is_null() {
        order = new Order(orderId, serviceName, null, callbackUrl, transactionId, customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_callbackUrl_is_empty() {
        order = new Order(orderId, serviceName, merchant, null, transactionId, customerId);

    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_callbackUrl_is_null() {
        order = new Order(orderId, serviceName, merchant, "    ", transactionId, customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_transactionId_is_empty() {
        order = new Order(orderId, serviceName, merchant, callbackUrl, null, customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_transactionId_is_null() {
        order = new Order(orderId, serviceName, merchant, callbackUrl, "   ", customerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_customerId_is_empty() {
        order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_customerId_is_null() {
        order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, "   ");
    }
    @Test
    public void checkIsFor_should_return_true_for_order_with_this_params(){
        order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, customerId);
        Assert.assertTrue(order.checkIsFor(transactionId,merchant.getMerchantId(),serviceName));
    }
    @Test
    public void checkIsFor_should_return_false_if_any_parameter_does_not_match(){
        order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, customerId);
        Assert.assertFalse(order.checkIsFor("diffTransactionID",merchant.getMerchantId(),serviceName));
        Assert.assertFalse(order.checkIsFor(transactionId,"diffMerchantId",serviceName));
        Assert.assertFalse(order.checkIsFor(transactionId,merchant.getMerchantId(),"diffServiceName"));
    }


}

