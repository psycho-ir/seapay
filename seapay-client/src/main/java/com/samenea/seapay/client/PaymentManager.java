package com.samenea.seapay.client;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 */
public interface PaymentManager {
    /**
     *
     * @param orderId orderId of the order which transaction is started for it
     * @param customerId customerId which transaction is started for
     * @return transactionId
     * @see OrderService
     * @throws com.samenea.seapay.client.impl.CommunicationException if can not connect to seapay
     */
    String startTransaction(String orderId, String customerId);

    /**
     * <p>will commit transaction on seapay client and calls {@link OrderService} when transaction is complete.</p>
     * <p>If there is a communication exception in committing
     * <p>If transaction is already {@link com.samenea.seapay.client.ws.TransactionStatus#COMMITED} commit on seapay will not be called again</p>
     * transaction on seapay (state of transaction is not known) in this case {@link OrderService#postponeDelivery(String, String)} will be called</p>
     * and this exception will be thrown
     * @param orderId
     * @param transactionId
     * @see OrderService
     * @throws com.samenea.seapay.client.impl.CommunicationException if there is a communication exception in committing
     * transaction on seapay in this case {@link OrderService#postponeDelivery(String, String)} will be called
     * and this exception will be thrown
     */
    void processTransaction(String orderId, String transactionId);
}
