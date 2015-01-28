package com.samenea.seapay.merchant;

/**
 * In merchant context transation is named as Order.
 * every order has some properties about erchant order that client want to have transaction for that.
 */
public interface IOrder {
    String getTransactionId();
    String getCallbackUrl();
    String getCustomerId();

    /**
     * checks if this parameters matches with this order info
     * @param transactionId
     * @param merchantId
     * @param serviceName
     * @return true if all parameters match this order
     *
     */
    //todo I think this is needed because of a flawed design
    boolean checkIsFor(String transactionId, String merchantId, String serviceName);
}
