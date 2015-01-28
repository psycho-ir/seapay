package com.samenea.seapay.security;

/**
 * A centralized service for authentication, management of security principals
 * @author Jalal Ashrafi
 */
public interface SecurityService {
    public void createAccount(String domain,String accountId,String accountPassword);
    public Boolean authenticate(String domain,String accountId,String accountPassword);
    public void enable(String domain,String accountId,String accountPassword);
    public void disable(String domain,String accountId,String accountPassword);
}
