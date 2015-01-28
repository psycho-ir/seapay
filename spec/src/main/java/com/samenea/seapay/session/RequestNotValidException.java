package com.samenea.seapay.session;


import com.samenea.commons.component.model.exceptions.*;

public class RequestNotValidException extends CommonModelRuntimeException {
	public RequestNotValidException() {
		super();
	}

	public RequestNotValidException(String s) {
		super(s);
	}

	public RequestNotValidException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public RequestNotValidException(Throwable throwable) {
		super(throwable);
	}
}
