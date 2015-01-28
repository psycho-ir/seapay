package com.samenea.seapay.bank.model;

import java.util.List;

/**
 * @author Jalal Ashrafi
 */
public interface BankRepository {
    public List<Ibank> getCustomerBanks(String customerId);

}
