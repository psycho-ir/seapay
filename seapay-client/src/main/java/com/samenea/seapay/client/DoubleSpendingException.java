package com.samenea.seapay.client;

/**
 * @author: Jalal Ashrafi
 * Date: 7/24/14
 */
public class DoubleSpendingException extends RuntimeException {
    public DoubleSpendingException() {
    }

    public DoubleSpendingException(String message) {
        super(message);
    }

    public DoubleSpendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoubleSpendingException(Throwable cause) {
        super(cause);
    }

    public DoubleSpendingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
