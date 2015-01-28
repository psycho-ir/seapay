package com.samenea.seapay.bank.gateway.model;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;

/**
 * Shows There is a network communication problem, it can be considered an unchecked counterpart for {@link java.rmi.RemoteException}
 * @author Jalal Ashrafi
 */
public class CommunicationException extends SamenRuntimeException {
    public CommunicationException(String s, Throwable cause) {
        super(s, cause);
    }

    public CommunicationException() {
        super();
    }

    public CommunicationException(String s) {
        super(s);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
