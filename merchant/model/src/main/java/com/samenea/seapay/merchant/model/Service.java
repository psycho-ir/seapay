package com.samenea.seapay.merchant.model;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import org.springframework.util.Assert;

import com.samenea.commons.component.model.Entity;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IService;

import java.text.MessageFormat;
import java.util.*;

import javax.persistence.ElementCollection;

/**
 * @author Jalal Ashrafi
 */
@javax.persistence.Entity
public class Service extends Entity<Long> implements IService {
    private final String serviceName;
    @ElementCollection(targetClass = BankAccount.class)
    private final Set<BankAccount> bankAccounts = new HashSet<BankAccount>();

    public Service() {
        serviceName = null;
    }

    public Service(String serviceName) {
        Assert.hasText(serviceName);
        this.serviceName = serviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service that = (Service) o;

        if (!serviceName.equals(that.serviceName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public List<IBankAccount> getBankAccounts() {
        return Collections.unmodifiableList(new ArrayList<IBankAccount>(bankAccounts));
    }

    @Override
    public IBankAccount getAccount(String bankName) {
        for (BankAccount bankAccount : this.bankAccounts) {
            if (bankAccount.getBankName().equals(bankName)) {
                return bankAccount;
            }
        }
        throw new NotFoundException(MessageFormat.format("BankAccount with bankName:{0} for service:{1} not found. ", bankName,this.getServiceName()));
    }


    public void addAccount(BankAccount bankAccount) {
        this.bankAccounts.add(bankAccount);
    }
}
