package com.samenea.seapay.bank.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.samenea.commons.component.model.Entity;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Map;

/**
 * @author Jalal Ashrafi
 */
@javax.persistence.Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_BankTransactionInfo", columnNames = {"transactionId", "bankName"})}
)
public class BankTransactionInfo extends Entity<Long> {
    private final String transactionId;
    //todo
    private final String bankName;

    @Column(length = 1000)
    @Type(type = "com.samenea.commons.model.repository.hibernate.StringAsMapUserType")
    private Map<String, String> paymentResponseParams;

    @Column(length = 1000)
    @Type(type = "com.samenea.commons.model.repository.hibernate.StringAsMapUserType")
    private Map<String, String> transactionStartParams;
    @Column(length = 40)
    private String referenceCode;

    public BankTransactionInfo(String transactionId, String bankName) {
        Assert.hasText(transactionId);
        Assert.hasText(bankName);
        paymentResponseParams = Maps.newHashMap();
        transactionStartParams = Maps.newHashMap();
        this.transactionId = transactionId;
        this.bankName = bankName;
    }

    private BankTransactionInfo() {
        this.bankName = null;
        this.transactionId = null;
    }

    public String getBankName() {
        return bankName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        BankTransactionInfo that = (BankTransactionInfo) o;
        if (!bankName.equals(that.bankName)) return false;
        if (!transactionId.equals(that.transactionId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionId.hashCode();
        result = 31 * result + bankName.hashCode();
        return result;
    }

    public void savePaymentResponseParams(Map<String, String> parameters) {
        this.paymentResponseParams = ImmutableMap.copyOf(parameters);
    }


    public Map<String, String> getPaymentResponseParams() {
        return paymentResponseParams;
    }

    public void saveTransactionStartParams(Map<String, String> parameters) {
        this.transactionStartParams = ImmutableMap.copyOf(parameters);
    }

    public Map<String, String> getTransactionStartParams() {
        return transactionStartParams;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
}
