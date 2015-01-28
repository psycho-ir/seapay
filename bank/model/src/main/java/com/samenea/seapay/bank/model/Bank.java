package com.samenea.seapay.bank.model;

import com.samenea.commons.component.model.Entity;

/**
 * @author Jalal Ashrafi
 */
@javax.persistence.Entity
public class Bank extends Entity<Long> implements Ibank {
    private final String bankName;

    protected Bank(){
        this.bankName=null;
    }
    public Bank(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank that = (Bank) o;

        if (!bankName.equals(that.bankName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bankName.hashCode();
    }
}
