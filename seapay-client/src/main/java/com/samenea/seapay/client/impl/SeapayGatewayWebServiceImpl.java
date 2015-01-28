package com.samenea.seapay.client.impl;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.client.SeapayGatewayWebService;
import com.samenea.seapay.client.ws.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: Jalal Ashrafi
 * Date: 1/16/13
 */
public class SeapayGatewayWebServiceImpl implements SeapayGatewayWebService {
    private ISeapayGatewayService seapayGatewayService;
    private Logger logger = LoggerFactory.getLogger(SeapayGatewayWebServiceImpl.class);
    private Logger exceptionLogger = LoggerFactory.getLogger(SeapayGatewayWebServiceImpl.class, LoggerFactory.LoggerType.EXCEPTION);


    @Autowired
    public SeapayGatewayWebServiceImpl(@Value("${seapay.webServiceAddress}") String wsdlLocation) {
        try {
            logger.info("About to create web service for: {}", wsdlLocation);
            this.seapayGatewayService = new SeapayGatewayService(new URL(wsdlLocation)).getSeapayGatewayServicePort();
            logger.info("Web service created successfully {}", wsdlLocation);
        } catch (MalformedURLException e) {
            logger.warn("Can not create seapay web service exception message is: {}", e.getMessage());
            exceptionLogger.warn("Can not create seapay web service exception message is: " + e.getMessage(), e);
            new RuntimeException(e);
        }
    }

    public String createSession(String merchantId, String serviceId, String password) {
        return seapayGatewayService.createSession(merchantId, serviceId, password);
    }

    @Override
    public String createTransaction(String sessionId, int amount, String description, String callbackUrl, String customerId) {
        return seapayGatewayService.createTransaction(sessionId, amount, description, callbackUrl, customerId);
    }

    @Override
    public void commitTransaction(String sessionId, String transactionId, int amount) throws AuthenticationException_Exception, IllegalStateException_Exception, CommunicationException_Exception, NotFoundException_Exception {
        seapayGatewayService.commitTransaction(sessionId, transactionId, amount);
    }

    @Override
    public TransactionStatus getTransactionStatus(String sessionId, String transactionId) throws NotFoundException_Exception, AuthenticationException_Exception {
        return seapayGatewayService.getTransactionStatus(sessionId, transactionId);
    }

    @Override
    public String getBankName(String sessionId, String transactionId) throws NotFoundException_Exception, AuthenticationException_Exception {
        return seapayGatewayService.getBankName(sessionId, transactionId);
    }

}
