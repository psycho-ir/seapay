package com.samenea.seapay.client;

/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 */

/**
 * An abstraction to be used as a call back mechanism on order system
 * for example to deliver order when transaction is committed or getting order amount
 * based on orderId
 */

public interface OrderService {
    /**
     * This method will be called when transaction is committed and implementation should deliver order
     * @param orderId orderId of the order
     * @param transactionId transactionId
     */
    void deliver(String orderId, String transactionId,String bankName);

    /**
     * return order amount for the order with this orderId
     * @param orderId
     * @return order amount for the order with this orderId
     */
    int orderAmount(String orderId);

    /**
     * When commit transaction state is not currently known this method will be called to postpone retrying transaction
     * process
     * @param orderId
     * @param transactionId
     */
    void postponeDelivery(String orderId, String transactionId);

    /***
     * @param orderId
     * Notice: It should be in a new transaction
     */
    void cancel(String orderId);

    /**
     * when transaction created for a checked out order this method
     * will be called thus order service can record transaction
     * todo this is a workaround for issue #IB-214 We need to have SEAPAY TransactionId before transfering to SEAPAY.
     * A better solution maybe is passing orderId to seapay for starting transaction but it needs much more work with
     * current design
     * @param orderId
     * @param transactionId
     */
    void assignTransaction(String orderId,String transactionId);

    void checkDoubleSpending(String orderId, String transactionId) throws DoubleSpendingException;
}
