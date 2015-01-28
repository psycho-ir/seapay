package com.samenea.seapay.transaction.model;

import com.samenea.commons.component.model.Entity;
import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.FraudException;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.transaction.IAccount;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;

/**
 * Model of transactions in SEAPAY system. This class represents banks
 * transaction model, use this class for creating bank transactions, communicate
 * with banks, and persist them.
 *
 * @author soroosh
 */
@Configurable(preConstruction = true, dependencyCheck = true)
@javax.persistence.Entity(name = "XTRANSACTION")
public class Transaction extends Entity<Long> implements ITransaction {
    public static final String ID_TOKEN = "TRN-";
    @Transient
    private final Logger logger = LoggerFactory.getLogger(Transaction.class);
    @Transient
    @Autowired(required = true)
    private Environment environment;
    @Transient
    @Autowired
    private IBankTransactionService bankTransactionService;
    @Transient
    @Autowired
    TransactionRepository transactionRepository;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "accountNumber", column = @Column(name = "accountNumber", nullable = true)), @AttributeOverride(name = "bankName", column = @Column(name = "bankName", nullable = true))})
    private Account transactionAccount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private Date lastUpdateDate;
    private final int amount;
    private Date startDate;
    private final String description;
    private final String transactionId;

    private Transaction() {
        this.amount = 0;
        this.description = "";
        this.transactionId = "";
    }

    public Transaction(String transactionId, int amount, String description) {
        Assert.notNull(transactionId, "TransactionId cannot be null.");
        Assert.hasText(transactionId, "TransactionId cannot be empty.");
        Assert.notNull(description, "Description cannot be null.");
        Assert.hasText(description, "Description cannot be empty.");
        final Date currentDate = this.environment.getCurrentDate();
        this.status = TransactionStatus.NEW;
        this.amount = amount;
        this.lastUpdateDate = currentDate;
        this.description = description;
        this.transactionId = transactionId;
    }

    @Override
    @Transactional
    public void mapToBank(IBankAccount transactionAccount) {
        logTransactionAvailability();
        if (!isStatusNew()) {
            throw new IllegalStateException("You can map bank just when transaction status is NEW.");
        }
        this.transactionAccount = new Account(transactionAccount.getAccountNumber(), transactionAccount.getBankName());
        this.status = TransactionStatus.BANK_RESOLVED;
        this.lastUpdateDate = environment.getCurrentDate();
        this.startDate = environment.getCurrentDate();

        transactionRepository.store(this);

    }

    /**
     * When transaction commitment recieved from bank, call this method for
     * updating {@link Transaction} status.
     *
     * @throws IllegalStateException when the {@link Transaction#getStatus()} is not
     *                               {@link TransactionStatus#BANK_RESOLVED}
     */

    @Override
    @Transactional
    public void commit() {
        logTransactionAvailability();
        checkIsOKForCommitment();
        this.lastUpdateDate = environment.getCurrentDate();
        final boolean commitResult = bankTransactionService.commitTransaction(this);
        if (!commitResult) {
            throw new IllegalStateException(String.format("Transation:%s could not be commited. Current status:%s", this.transactionId, this.status));
        }

        this.status = TransactionStatus.COMMITED;
        transactionRepository.store(this);
        logger.info("Transaction Committed {}", transactionId);
    }

    private void checkIsOKForCommitment() {
        if (this.status == TransactionStatus.UNKNOWN || this.status == TransactionStatus.BANK_RESOLVED) {
            return;
        }
        throw new IllegalStateException("Current status of transaction " + this.transactionId + " is " + this.status + " but must be BANK_RESOLVED or UNKNOWN");

    }

    /**
     * When transaction decliend from bank, call this method for updating
     * {@link Transaction} status.
     *
     * @throws IllegalStateException when the {@link Transaction#getStatus()} is not
     *                               {@link TransactionStatus#COMMITED}
     */
    @Transactional
    public void cancel() {
        logTransactionAvailability();
        if (isStatusCOMMITED()) {
            throw new IllegalStateException("Current status of transaction " + this.transactionId + " is COMMITTED so you can't cancel this.");
        }
        logger.info("Transaction: {}  is rolled back. Old Status: {}  ", this.transactionId, this.getStatus());
        this.status = TransactionStatus.FAILED;
        this.lastUpdateDate = environment.getCurrentDate();
        transactionRepository.store(this);
    }

    @Transactional
    public void markUnknown() {
        logTransactionAvailability();

        if (this.status != TransactionStatus.BANK_RESOLVED) {
            throw new IllegalStateException("Current status of transaction " + this.transactionId + " is not in BANK_RESOLVED so you can't mark as unknown.");
        }
        this.status = TransactionStatus.UNKNOWN;
        this.lastUpdateDate = environment.getCurrentDate();
        transactionRepository.store(this);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,noRollbackFor = FraudException.class)
    public void investigate() {
        if (this.status != TransactionStatus.UNKNOWN && this.status != TransactionStatus.BANK_RESOLVED) {
            throw new IllegalStateException(String.format("Transaction {0} is {1} but must be UNKNOWN or BANK_RESOLVED", this.transactionId, this.status));
        }
        try {
            boolean pollResult = bankTransactionService.pollTransaction(this);
            if (pollResult) {
                this.status = TransactionStatus.COMMITED_WITH_DELAY;
            } else {
                this.status = TransactionStatus.FAILED;

            }
        } catch (FraudException fe) {
            logger.error("Fraud detected: " + fe.getMessage());
            this.status = TransactionStatus.FRAUD;
        }
        this.lastUpdateDate = environment.getCurrentDate();
        transactionRepository.store(this);
    }


    public IAccount getTransactionAccount() {
        return transactionAccount;
    }

    @Override
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }


    @Override
    public String getDescription() {
        return description;
    }


    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public long getTransactionNumber() {
        final int startIndex = ID_TOKEN.length();
        return Long.parseLong(this.transactionId.substring(startIndex));
    }

    @Override
    public com.samenea.seapay.transaction.TransactionStatus getStatus() {
        return status;
    }

    @Override
    public String getAccountNumber() {
        return transactionAccount == null ? null : this.transactionAccount.getAccountNumber();
    }

    @Override
    public String getBankName() {
        return transactionAccount == null ? null : this.transactionAccount.getBankName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        } else if (!transactionId.equals(other.transactionId))
            return false;
        return true;
    }

    private boolean isStatusCOMMITED() {
        return this.status == TransactionStatus.COMMITED;
    }

    private boolean isStatusBANK_RESOLVED() {
        return this.status == TransactionStatus.BANK_RESOLVED;
    }

    private boolean isStatusNew() {
        return this.status == TransactionStatus.NEW;
    }

    //TODO check availability check in a separate util $review (we need a test for transaction availability checks in commons)
    private void logTransactionAvailability() {
        if (logger.isDebugEnabled()) {
            logger.debug("Is transaction active? {}", TransactionSynchronizationManager.isActualTransactionActive());
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountNumber=" + getAccountNumber() +
                ", status=" + status +
                ", lastUpdateDate=" + lastUpdateDate +
                ", amount=" + amount +
                ", startDate=" + startDate +
                ", description='" + description + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
