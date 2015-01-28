package com.samenea.seapay.web.model;

import com.samenea.commons.component.utils.persian.NumberToWritten;
import com.samenea.commons.component.utils.persian.NumberUtil;
import com.samenea.seapay.transaction.model.Transaction;

/**
 * Date: 1/29/13
 * Time: 11:13 AM
 *
 * @Author:payam
 */
public class TransactionViewModel {
    private String amount;
    private String amountDepositLetter;
    private String transactionId;
    private String description;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public TransactionViewModel(int amount,String transactionId,String description) {

        this.amount = String.valueOf(amount);
        this.amountDepositLetter = NumberToWritten.convert(amount);
        this.transactionId = transactionId;
    }

    public TransactionViewModel() {
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }



    public String getAmountDepositLetter() {
        return amountDepositLetter;
    }

    public void setAmountDepositLetter(String amountDepositLetter) {
        this.amountDepositLetter = amountDepositLetter;
    }

    public TransactionViewModel(Transaction transaction){
        this.amount = String.valueOf(transaction.getAmount());
        this.description = transaction.getDescription();
        this.status = transaction.getStatus().name();
        this.transactionId = transaction.getTransactionId();
        this.amountDepositLetter = "I don't know!";
    }

}
