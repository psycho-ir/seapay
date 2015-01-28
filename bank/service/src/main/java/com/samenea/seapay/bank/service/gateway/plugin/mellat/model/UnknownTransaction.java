package com.samenea.seapay.bank.service.gateway.plugin.mellat.model;

import com.samenea.commons.component.model.Entity;
import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.BankTransactionInfoRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Some times we don't know the status of transactions, so we need to persist them and clarify their status.
 * UnknownTransaction is the model of these kinds of transactions.
 *
 * @author: Soroosh Sarabadani
 * Date: 1/8/13
 * Time: 5:00 PM
 */

@Configurable(preConstruction = true, dependencyCheck = true)
@javax.persistence.Entity
public class UnknownTransaction extends Entity<Long> {
    @Transient
    private static final Logger logger = LoggerFactory.getLogger(UnknownTransaction.class);

    @Column(nullable = false, unique = true, updatable = false)
    private final String transactionId;
    @Column(nullable = false, updatable = false)
    private final String accountNumber;
    @Enumerated(EnumType.STRING)
    private UnknownTransactionStatus status;
    @Column(nullable = false, updatable = false)
    private final Date createDate;
    @Column
    private Date lastUpdateDate;

    @Transient
    @Autowired(required = true)
    private Environment environment;
    @Transient
    @Autowired(required = true)
    private BankTransactionInfoRepository bankTransactionInfoRepository;

    protected UnknownTransaction() {
        this.transactionId = null;
        this.createDate = null;
        this.accountNumber = null;
    }

    public UnknownTransaction(String transactionId, String accountNumber) {
        Assert.hasText(transactionId, "transactionId cannot be empty");
        Assert.notNull(transactionId, "transactionId cannot be null.");
        Assert.hasText(accountNumber, "accountNumber cannot be empty");
        Assert.notNull(accountNumber, "accountNumber cannot be null.");

        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.status = UnknownTransactionStatus.SETTLE_PROBLEM;
        this.createDate = environment.getCurrentDate();
    }

    public UnknownTransactionStatus getStatus() {
        return status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void investigate() throws CommunicationException, VerifyException {
        if (isNotOKForSettlment()) {
            throw new IllegalStateException(String.format("UnknownTransaction must not be in {0} for investigating.", this.status));
        }
        final BankTransactionInfo bankTransactionInfo = bankTransactionInfoRepository.findByTransactionId(this.transactionId);

        try {
            new SettleProblemInvestigator(bankTransactionInfo, accountNumber).investigate();
            this.status = UnknownTransactionStatus.SOLVED;
        } catch (VerifyException e) {
            this.status = UnknownTransactionStatus.UNRESOLVED;
            logger.error(e.getMessage());
        }

        this.lastUpdateDate = environment.getCurrentDate();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnknownTransaction other = (UnknownTransaction) obj;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        } else if (!transactionId.equals(other.transactionId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        return result;
    }

    private boolean isNotOKForSettlment() {
        return this.status != UnknownTransactionStatus.SETTLE_PROBLEM;
    }
}
