package com.samenea.seapay.transaction.exceptions;



import com.samenea.commons.component.model.exceptions.CommonModelException;

public class GatewayNotFoundException extends CommonModelException {
	public GatewayNotFoundException() {
		super();
	}

	public GatewayNotFoundException(String s) {
		super(s);
	}

	public GatewayNotFoundException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public GatewayNotFoundException(Throwable throwable) {
		super(throwable);
	}
}
