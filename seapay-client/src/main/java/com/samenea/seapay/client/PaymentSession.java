package com.samenea.seapay.client;

import com.samenea.seapay.client.ws.TransactionStatus;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 * Represent a seapay payment session. A merchant should use a payment session for every service he has
 * By 'service' we mean seapay service i.e. each merchant can use
 */
public interface PaymentSession {
    /**
     * Create a payment transaction on seapay and returns created transaction's transactionId
     * @param amount transaction amount
     * @param description transaction description
     * @param callbackUrl call back url which transacion answer will be returned to
     * @param customerId customerId
     * @return created transaction's transactionId
     * @throws com.samenea.seapay.client.impl.CommunicationException if can not communicate to seapay for example when
     * there is a connection exception
     */
    String createTransaction(int amount, String description, String callbackUrl, String customerId);

    /**
     * commits a transaction, amount passed is just as a safety net and is for double check
     * @param transactionId transaction's transactionId
     * @param amount transaction expected amount
     * @throws IllegalStateException if transaction status is not {@link TransactionStatus#BANK_RESOLVED}
     * @throws com.samenea.seapay.client.impl.CommunicationException if can not communicate to seapay for example when
     * there is a connection exception
     * @throws com.samenea.commons.component.model.exceptions.NotFoundException if there is no such transaction
     */
    void commitTransaction(String transactionId, int amount)throws IllegalStateException;

    /**
     * returns transaction status
     * @param transactionId transaction's transactionId
     * @return status of transaction
     * @throws com.samenea.commons.component.model.exceptions.NotFoundException if there is no such transaction
     * @throws com.samenea.seapay.client.impl.CommunicationException if can not communicate to seapay for example when
     * there is a connection exception
     */
    TransactionStatus getTransactionStatus(String transactionId);

    String getBankName(String transactionId);
}
