package com.samenea.seapay.bank.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.collections.collection.UnmodifiableCollection;

import com.samenea.commons.component.model.ValueObject;

@Embeddable
public class PaymentGatewayConfiguration extends ValueObject {
    @Transient
	private static final String KEY_VALUE_DELIMETER = "!";
    @Transient
	private static final String ITEM_DELIMETER = ";";

	@Column(nullable = false)
	private String config;

	@Transient
	Map<String, String> configKeyValues;

	protected PaymentGatewayConfiguration() {
		this.configKeyValues = new HashMap<String, String>();
	}

	public PaymentGatewayConfiguration(String config) {
		this();
		this.config = config;
		createConfigKeyValues();
	}

	private void createConfigKeyValues() {
		String[] items = this.config.split(ITEM_DELIMETER);
		this.configKeyValues.clear();
		for (String item : items) {
			String[] keyValue = item.split(KEY_VALUE_DELIMETER);
			if (!isKeyValueValid(keyValue)) {
				throw new IllegalArgumentException("ConfigString has problem in key/values");
			}
			this.configKeyValues.put(keyValue[0], keyValue[1]);
		}
	}

	private boolean isKeyValueValid(String[] keyValue) {
		return keyValue.length == 2;
	}

	public Map<String, String> getConfigKeyValues() {
		if (this.configKeyValues.size() == 0) {
			createConfigKeyValues();
		}
		return Collections.unmodifiableMap(configKeyValues);
	}

	public String get(String key) {
		if (this.configKeyValues.size() == 0) {
			createConfigKeyValues();
		}
		return this.configKeyValues.get(key);
	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
