package com.samenea.seapay.bank.utils.model;

import com.samenea.commons.component.model.Entity;
import org.springframework.util.Assert;

import javax.persistence.Column;

/**
 * @author: Jalal Ashrafi
 * Date: 6/19/13
 */
@javax.persistence.Entity
public class CutOffItem extends Entity<Long> {
    @Column(nullable = false)
    private final String terminalCode;
    /*    @Column(nullable = false)
        private final String accountNumber;*/
    private final String merchantCode;
    private final String installDateTransaction;
    private final String transactionTime;
    @Column(unique = true, nullable = false)
    private final String referNumber;
    private final String transactionDescription;
    @Column(nullable = false)
    private final long transactionAmount;
    private final String followCode;
    private final String bank;
    private final String pan;

    private CutOffItem() {
        pan = null;
        followCode = null;
        bank = null;
        transactionAmount = 0;
        referNumber = null;
        transactionDescription = null;
        transactionTime = null;
        installDateTransaction = null;
//        accountNumber = null;
        merchantCode = null;
        terminalCode = null;
    }

    public CutOffItem(String terminalCode, String merchantCode, String installDateTransaction, String transactionTime, String referNumber, String transactionDescription, long transactionAmount, String followCode, String bank, String pan) {
        this.transactionTime = transactionTime;
        this.pan = pan;
//        Assert.hasText(accountNumber,"accountNumber should not be null or empty.");
        Assert.hasText(merchantCode, "merchantCode should not be null or empty.");
        Assert.hasText(referNumber, "referNumber should not be null or empty.");
        Assert.notNull(transactionAmount, "transactionAmount should not be null.");
        this.terminalCode = terminalCode;
//        this.accountNumber = accountNumber;
        this.merchantCode = merchantCode;
        this.installDateTransaction = installDateTransaction;
        this.referNumber = referNumber;
        this.transactionDescription = transactionDescription;
        this.transactionAmount = transactionAmount;
        this.followCode = followCode;
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CutOffItem that = (CutOffItem) o;

        if (transactionAmount != that.transactionAmount) return false;
//        if (!accountNumber.equals(that.accountNumber)) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (followCode != null ? !followCode.equals(that.followCode) : that.followCode != null) return false;
        if (installDateTransaction != null ? !installDateTransaction.equals(that.installDateTransaction) : that.installDateTransaction != null)
            return false;
        if (!merchantCode.equals(that.merchantCode)) return false;
        if (!referNumber.equals(that.referNumber)) return false;
        if (terminalCode != null ? !terminalCode.equals(that.terminalCode) : that.terminalCode != null) return false;
        if (transactionDescription != null ? !transactionDescription.equals(that.transactionDescription) : that.transactionDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = terminalCode != null ? terminalCode.hashCode() : 0;
//        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + merchantCode.hashCode();
        result = 31 * result + (installDateTransaction != null ? installDateTransaction.hashCode() : 0);
        result = 31 * result + referNumber.hashCode();
        result = 31 * result + (transactionDescription != null ? transactionDescription.hashCode() : 0);
        result = 31 * result + (int) (transactionAmount ^ (transactionAmount >>> 32));
        result = 31 * result + (followCode != null ? followCode.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SaderatRow{" +
                "terminalCode='" + terminalCode + '\'' +
//                ", accountNumber='" + accountNumber + '\'' +
                ", merchantCode='" + merchantCode + '\'' +
                ", installDateTransaction='" + installDateTransaction + '\'' +
                ", referNumber='" + referNumber + '\'' +
                ", transactionDescription='" + transactionDescription + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", followCode='" + followCode + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }

    public String getTerminalCode() {
        return terminalCode;
    }
/*
    public String getAccountNumber() {
        return accountNumber;
    }*/

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getInstallDateTransaction() {
        return installDateTransaction;
    }

    public String getReferNumber() {
        return referNumber;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public long getTransactionAmount() {
        return transactionAmount;
    }

    public String getFollowCode() {
        return followCode;
    }

    public String getBank() {
        return bank;
    }
}
