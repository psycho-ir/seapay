package com.samenea.seapay.transaction;

import java.util.List;

/**
 * implement this interface for creating a manager that will handle requests from Merchant Sites.
 * class must check and validate requests.
 * must find GatewayPlugin that is suitable for creating response.
 * @author soroosh
 *
 */
public interface ITransactionService {
    /**
     *
     * this method should not return null.
     * @param transactionId
     * @return
     */
	 ITransaction getTransaction(String transactionId);

    /**
     * Finds all transactions by status
     */
    List<? extends ITransaction> findTransactionsByStatus(TransactionStatus status);
}



