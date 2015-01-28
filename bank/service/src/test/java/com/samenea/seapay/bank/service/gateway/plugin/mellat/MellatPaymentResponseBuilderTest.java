package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.seapay.bank.gateway.model.RedirectData;
import junit.framework.Assert;

import org.junit.Test;

import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;

import static junit.framework.Assert.assertEquals;

public class MellatPaymentResponseBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void should_not_accept_not_ok_mellatResponse() {
        MellatWSResponse notOkResponse = new MellatWSResponse("1,abduidhs");
		new RedirectDataBuilder(notOkResponse);
	}

	@Test
	public void should_make_method_POST_and_parameter_RefId() {
        String  refId = "abduidhs";
        MellatWSResponse response = new MellatWSResponse("0," + refId);
		RedirectData redirectData = new RedirectDataBuilder(response).createPaymentResponse();

		assertEquals(RedirectData.HttpMethod.POST, redirectData.getHttpMethod());
		assertEquals(1, redirectData.getParameters().size());
		assertEquals(refId, redirectData.getParameters().get(RedirectDataBuilder.REFID));
		Assert.assertNotNull(redirectData.getUrl());
	}

}
