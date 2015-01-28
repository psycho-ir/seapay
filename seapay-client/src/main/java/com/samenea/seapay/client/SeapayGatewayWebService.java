package com.samenea.seapay.client;


import com.samenea.seapay.client.ws.*;

/**
 * @author: Jalal Ashrafi
 * Date: 1/16/13
 */
public interface SeapayGatewayWebService {
    public String createSession(String merchantId, String serviceId, String password);

    String createTransaction(String sessionId, int amount, String description, String callbackUrl, String customerId);

    void commitTransaction(String sessionId, String transactionId, int amount) throws AuthenticationException_Exception, IllegalStateException_Exception, CommunicationException_Exception, NotFoundException_Exception;

    TransactionStatus getTransactionStatus(String sessionId, String transactionId) throws NotFoundException_Exception, AuthenticationException_Exception;

    String getBankName(String sessionId, String transactionId) throws NotFoundException_Exception, AuthenticationException_Exception;
}
