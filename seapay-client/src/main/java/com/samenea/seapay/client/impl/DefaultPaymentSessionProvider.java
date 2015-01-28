package com.samenea.seapay.client.impl;

import com.samenea.seapay.client.PaymentSession;
import com.samenea.seapay.client.PaymentSessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 * Currently it is not needed
 */
@Component
@Configurable(dependencyCheck = true)
@Deprecated
public class DefaultPaymentSessionProvider  implements PaymentSessionProvider {

    private static DefaultPaymentSessionProvider instance;


    private DefaultPaymentSessionProvider() {
    }
    @Autowired
    public static synchronized PaymentSessionProvider getInstance() {
        if(instance == null)
        {
            instance = new DefaultPaymentSessionProvider();
        }
        return instance;
    }

    @Override
    public PaymentSession provideSession(String merchantId,String serviceName, String merchantPassword) {
        return new DefaultPaymentSession(merchantId,serviceName,merchantPassword);
    }
}
