package com.samenea.seapay.bank.gateway.model;

import com.samenea.commons.component.model.exceptions.CommonModelRuntimeException;

/**
 * Raise when Gateway name is wrong and system cannot find gateway.
 */
public class GatewayNotFoundException extends CommonModelRuntimeException {


	public GatewayNotFoundException(String s) {
		super(s);
	}

}
