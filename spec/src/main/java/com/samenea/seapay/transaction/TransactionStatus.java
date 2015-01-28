package com.samenea.seapay.transaction;

public enum TransactionStatus {
    NEW,
    BANK_RESOLVED,
    COMMITED,
    FAILED,
    UNKNOWN,
    COMMITED_WITH_DELAY,
    FRAUD;
}
