package com.samenea.seapay.merchant;


public interface IOrderService {

    /**
     * Returns the service of transaction, Because service is in another aggregate with this method
     * facilitate the access to service.
     * @param transactionId
     * @return the service of transaction
     */
	IService findServiceOfTransaction(String transactionId);

    /**
     * This method finds the order of transaction. IOrder is the meaning of transaction in merchant module.
     * @param transactionId Id of transaction that you need its order.
     * @return Order correspond to transactionId
     */

    IOrder findOrderByTransactionId(String transactionId);
}
