package com.samenea.seapay.bank.service.exceptions;

import com.samenea.commons.component.model.exceptions.CommonModelException;

public class NotExisTransaction extends CommonModelException {
    public NotExisTransaction() {
        super();    
    }

    public NotExisTransaction(String s) {
        super(s);    
    }

    public NotExisTransaction(String s, Throwable throwable) {
        super(s, throwable);    
    }

    public NotExisTransaction(Throwable throwable) {
        super(throwable);    
    }
}
