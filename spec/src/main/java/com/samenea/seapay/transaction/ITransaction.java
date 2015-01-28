package com.samenea.seapay.transaction;

import com.samenea.seapay.merchant.IBankAccount;

public interface ITransaction extends TransactionInfo {
    /**
     * After transaction creation, this method specify the bank and accounter
     * number for transaction.
     *
     * @param bankAccount
     * @throws IllegalStateException when {@link ITransaction#getStatus()} is not
     *                               {@link TransactionStatus#NEW}.
     * @throws IllegalStateException when {@link IAccount#getAccountNumber()} or
     *                               {@link IAccount#getBankName()} is not consistent with current
     *                               service of transaction.
     */
    //todo It can be removed from this interface by changing the design I think $review5
    void mapToBank(IBankAccount bankAccount);

    void commit();

    void markUnknown();

    void cancel();

}
