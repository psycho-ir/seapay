package com.samenea.seapay.session.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.transaction.ITransactionService;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.merchant.IMerchant;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.model.ITransactionFactory;

@Configurable(preConstruction = true, dependencyCheck = true)
@Entity(name = "XSESSION")
public class Session extends com.samenea.commons.component.model.Entity<Long> implements ISession {
	@Transient
	private static final Logger logger = LoggerFactory.getLogger(Session.class);

	@Autowired
	@Transient
	private IMerchantService merchantService;
	@Autowired
	@Transient
	private Environment environment;
	@Autowired
	@Transient
	private ITransactionFactory transactionFactory;
    @Autowired
    @Transient
    private ITransactionService transactionService;

	@Column(nullable = false)
	private final String merchantId;
	@Column(nullable = false)
	private final String sessionId;
	@Column(nullable = false)
	private final String serviceId;
	@Column(nullable = false)
	private final Date createDate;

    public Session() {
		this.sessionId = "";
		this.merchantId = "";
		this.serviceId = "";
		this.createDate = null;
	}

	public Session(String merchantId, String sessionId, String serviceId) {
		Assert.notNull(merchantId, "Merchant ID cannot be null.");
		Assert.hasText(merchantId, "Merchant ID cannot be empty.");
		Assert.notNull(sessionId, "Session ID cannot be null.");
		Assert.hasText(sessionId, "Session ID cannot be empty.");
		Assert.notNull(serviceId, "Service ID cannot be null.");
		Assert.hasText(serviceId, "Service ID cannot be empty.");
		this.merchantId = merchantId;
		this.sessionId = sessionId;
		this.serviceId = serviceId;
		this.createDate = environment.getCurrentDate();
	}

	@Override
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public String getMerchantId() {
		return this.merchantId;
	}

	@Override
	public Date getCreateDate() {
		return this.createDate;
	}

	@Override
	public String getServiceId() {
		return this.serviceId;
	}

	// TODO: equals and hashCode after stabilization must implement
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
	@Transactional
	public ITransaction createTransaction(int amount, String description, String callbackUrl, String customerId) {
		logTransactionAvailability();
		ITransaction transaction = transactionFactory.createTransaction(amount, description);
        MDC.put("transactionId", transaction.getTransactionId());
        logger.info("Transaction created: {}", transaction.toString());
		IMerchant merchant = merchantService.getMerchant(this.merchantId);
        final IOrder order = merchant.createOrder(this.serviceId, transaction.getTransactionId(), callbackUrl, customerId);
        logger.info("Order created : {}",order.toString());
        //there is no way to access to order info here
		return transaction;
	}

	@Override
	@Transactional
	public void commit(String transactionId, int amount) {
		logTransactionAvailability();
        logger.info("Start to commit transaction {}",transactionId);
        final ITransaction transaction = transactionService.getTransaction(transactionId);
        if (transaction.getAmount() != amount) {
            logger.warn("transaction {} amount is not equals with commit amount. request:{} <> current:{}", transactionId, amount, transaction.getAmount());
            throw new RequestNotValidException("transaction amount is not equals with commit amount.");
        }
        transaction.commit();
    }

	private void logTransactionAvailability() {
		if (logger.isDebugEnabled()) {
			logger.debug("Is transaction active? {}", TransactionSynchronizationManager.isActualTransactionActive());
		}
	}

    @Override
    public String toString() {
        return "Session{" +
                "merchantId='" + merchantId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", creationDate=" + createDate +
                '}';
    }
}
