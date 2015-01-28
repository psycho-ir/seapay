package com.samenea.seapay.bank.service.gateway.plugin.mellat.ws;

import junit.framework.Assert;

import org.junit.Test;

public class MellatWSResponseTest {

	private final String code = "0 , 3232";

	@Test
	public void should_isOK_return_true_when_resultCode_is_0() {
		MellatWSResponse response = new MellatWSResponse(code);
		Assert.assertTrue(response.isOK());
	}

	@Test
	public void should_isOK_reurn_false_when_resultCode_is_not_0() {
		MellatWSResponse response = new MellatWSResponse("1,222");
		Assert.assertFalse(response.isOK());
	}


	@Test
	public void should_getReferenceId_return_referenceId_when_response_isOK() {
		MellatWSResponse response = new MellatWSResponse(code);

		Assert.assertEquals("3232", response.getReferenceId());

	}

	@Test
	public void should_getReferenceId_return_empty_when_response_is_not_OK() {
		MellatWSResponse response = new MellatWSResponse("1,222");

		Assert.assertEquals("", response.getReferenceId());

	}
}
