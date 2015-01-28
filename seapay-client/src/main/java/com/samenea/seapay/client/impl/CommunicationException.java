package com.samenea.seapay.client.impl;

/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 * //todo should be moved to commons
 */
public class CommunicationException extends RuntimeException{
    public CommunicationException() {
    }

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
