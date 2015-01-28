package com.samenea.seapay.bank.service.exceptions;

import com.samenea.commons.component.model.exceptions.CommonModelException;

public class ServiceNotFoundException extends CommonModelException {


	public ServiceNotFoundException() {
        super();    
    }

    public ServiceNotFoundException(String s) {
        super(s);    
    }

    public ServiceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);    
    }

    public ServiceNotFoundException(Throwable throwable) {
        super(throwable);    
    }

}
