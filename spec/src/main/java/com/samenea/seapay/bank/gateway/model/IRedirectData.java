package com.samenea.seapay.bank.gateway.model;

import com.samenea.seapay.bank.model.IBankTransactionService;

import java.util.Map;

/**
 * On {@link IBankTransactionService#startTransaction(String, com.samenea.seapay.merchant.IBankAccount)}
 * by returning on object of this type payment gateway defines the redirect method, where the redirect should be done, and parameters
 * @author Jalal Ashrafi
 */
public interface IRedirectData {
    /**
     * key values which should be sent on bank payment page
     * @return key values
     */
    Map<String, String> getParameters();

    /**
     * How redirect should be done.POST, GET ...
     * @see HttpMethod
     */
    HttpMethod getHttpMethod();

    /**
     * To which url redirect should be done
     * @return
     */
    String getUrl();

    public static enum HttpMethod{
        POST,GET
    }
}
