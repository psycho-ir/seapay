package com.samenea.seapay.merchant;

import java.util.List;

public interface IMerchant {

	IOrder createOrder(String serviceName, String transactionId, String callbackUrl, String customerId);

    String getPassword();

    List<? extends IService> getServices();

    boolean hasService(String serviceName);
}
