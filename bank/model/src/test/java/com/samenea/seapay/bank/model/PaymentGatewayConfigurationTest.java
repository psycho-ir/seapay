package com.samenea.seapay.bank.model;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;

public class PaymentGatewayConfigurationTest {

	private PaymentGatewayConfiguration paymentGatewayConfiguration;
	private String config = "username!soroosh;password!123";

	@Test
	public void should_return_map_of_key_values_correspond_to_configString() {
		paymentGatewayConfiguration = new PaymentGatewayConfiguration(config);

		Map<String, String> result = paymentGatewayConfiguration.getConfigKeyValues();
		Assert.assertEquals(2, result.size());
	}

	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_when_configString_is_not_ok() {
		config = "username@@soroosh;password";
		paymentGatewayConfiguration = new PaymentGatewayConfiguration(config);
	}
}
