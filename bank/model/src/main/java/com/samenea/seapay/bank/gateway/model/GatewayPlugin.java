package com.samenea.seapay.bank.gateway.model;

import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.PaymentResponseCode;
import com.samenea.seapay.transaction.TransactionInfo;

import java.util.Map;

/**
 * with implementing this interface you have a new GatewayPlugin that client
 * applications can use.
 *
 * @author soroosh
 */
public interface GatewayPlugin {

    // /**
    // * create a response meta data for creating specific request for sending
    // to PaymentGateway.
    // * @param serviceId
    // * @param amount
    // * @param extraApplicationData
    // * @return
    // */
    IRedirectData request(TransactionInfo transaction);
    // /**
    // * confirm specific transaction
    // * financial transaction must commit or cancel in this method.
    // * @param transactionId
    // */
    // Transaction confirm(String transactionId);
    // /**
    // * the status of transaction must retrieved.
    // * @param transactionId
    // */
    // Transaction trackTransaction(String transactionId);

    boolean verify(TransactionInfo transaction, Map<String, String> responseParameters);

    /**
     * @param transactionInfo
     * @param parameters
     * @return true if parameter shows a successfull transaction and false otherwise
     */
    PaymentResponseCode interpretResponse(TransactionInfo transactionInfo, Map<String, String> parameters);

    /**
     * will query payment gateway that the current transactim is commited or no.
     *
     * @param transactionInfo
     * @return if transaction commited this will return true else false
     */
    boolean isCommited(TransactionInfo transactionInfo, BankTransactionInfo bankTransactionInfo);

    String getReferenceCodeFromResponse(Map<String, String> parameters);


}
