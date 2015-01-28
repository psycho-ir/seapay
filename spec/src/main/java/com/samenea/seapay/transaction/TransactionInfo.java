package com.samenea.seapay.transaction;

import java.util.Date;

/**
 * Provides a read only view on Transaction information
 * @author Jalal Ashrafi
 */
public interface TransactionInfo {
    Date getLastUpdateDate();

    int getAmount();

    Date getStartDate();

    String getDescription();

    String getTransactionId();

    long getTransactionNumber();

    TransactionStatus getStatus();

    String getAccountNumber();
    
    String getBankName();
}
