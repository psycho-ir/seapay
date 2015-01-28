package com.samenea.seapay.bank.model;

import com.samenea.commons.component.model.Entity;

import javax.persistence.*;
import java.lang.annotation.Annotation;

/**
 * @author Jalal Ashrafi
 *
 */
//todo how about balance field in this class $review
@javax.persistence.Entity
public class BankAccount extends Entity<Long> {
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "config", column = @Column(name = "config", nullable = false)) })
	private PaymentGatewayConfiguration paymentGatewayConfiguration;

    @ManyToOne
    private Bank bank;
    @Column(nullable = false)
    private String accountNumber;

	public void setPaymentGatewayConfiguration(PaymentGatewayConfiguration paymentGatewayConfiguration) {
		this.paymentGatewayConfiguration = paymentGatewayConfiguration;
	}

	public PaymentGatewayConfiguration getPaymentGatewayConfiguration() {
		return paymentGatewayConfiguration;
	}

    @Override
    public boolean equals(Object obj) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
