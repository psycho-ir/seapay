package com.samenea.seapay.merchant.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import com.samenea.seapay.merchant.IOrder;
import org.springframework.util.Assert;

import com.samenea.commons.component.model.Entity;

@javax.persistence.Entity(name="XORDER")
public class Order extends Entity<Long> implements IOrder {
	public static final String TOKEN_ID = "ORDER-";
	@Column(unique = true)
	private final String orderId;
	@Column(nullable = false)
	private final String serviceName;
	@ManyToOne
	@JoinColumn(nullable = false)
	private final Merchant merchant;
	@Column(nullable = false)
	private final String callbackUrl;
	@Column(unique = true)
	private final String transactionId;
	private final String customerId;

	protected Order() {
		orderId = null;
		serviceName = null;
		merchant = null;
		callbackUrl = null;
		transactionId = null;
		customerId = null;
	}

	public Order(String orderId, String serviceName, Merchant merchant, String callbackUrl, String transactionId, String customerId) {
		Assert.notNull(orderId, "orderId cannot be null.");
		Assert.hasText(orderId, "orderId cannot be empty.");
		Assert.notNull(serviceName, "service cannot be null.");
		Assert.notNull(serviceName, "service cannot be empty.");
		Assert.notNull(merchant, "merchant cannot be null.");
		Assert.notNull(callbackUrl, "callbackUrl cannot be null.");
		Assert.hasText(callbackUrl, "callbackUrl cannot be empty.");
		Assert.notNull(transactionId, "transactionId cannot be null.");
		Assert.hasText(transactionId, "transactionId cannot be empty.");
		Assert.notNull(customerId, "customerId cannot be null.");
		Assert.hasText(customerId, "customerId cannot be empty.");
		this.orderId = orderId;
		this.serviceName = serviceName;
		this.merchant = merchant;
		this.callbackUrl = callbackUrl;
		this.transactionId = transactionId;
		this.customerId = customerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	// TODO: should implements there methods after stabilization
	@Override
	public boolean equals(Object obj) {
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
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", merchant=" + merchant.getMerchantId() +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }

    public boolean checkIsFor(String transactionId, String merchantId, String serviceName) {
        return this.transactionId.equals(transactionId)
                && this.merchant.getMerchantId().equals(merchantId)
                && this.serviceName.equals(serviceName);
    }
}
