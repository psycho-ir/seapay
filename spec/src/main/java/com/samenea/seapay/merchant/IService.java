package com.samenea.seapay.merchant;

import java.util.List;

/**
 * Every merchant can have many system in SEAPAY.
 * Service is a concept that shows which banks can serve for current service.
 * Merchant can have seperated account for every service.
 *
 * @author Jalal Ashrafi
 */
public interface IService {
    String getServiceName();

    List<IBankAccount> getBankAccounts();

    IBankAccount getAccount(String bankName);
}
