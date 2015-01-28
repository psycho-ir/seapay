package com.samenea.seapay.client;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 * Currently it is not needed, may be later!
 */
@Deprecated
public interface PaymentSessionProvider {
    PaymentSession provideSession(String merchantId,String serviceName, String merchantPassword);
}
