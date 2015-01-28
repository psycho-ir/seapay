package com.samenea.seapay.bank.gateway.model;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;

/**
 * Created by soroosh on 8/3/14.
 */
public class FraudException extends SamenRuntimeException {
    public FraudException(String message){
        super(message);
    }
}
