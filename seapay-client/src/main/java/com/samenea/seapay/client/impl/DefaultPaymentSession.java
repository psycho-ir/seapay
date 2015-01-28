package com.samenea.seapay.client.impl;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.client.PaymentSession;
import com.samenea.seapay.client.SeapayGatewayWebService;
import com.samenea.seapay.client.ws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.ws.WebServiceException;
import java.lang.IllegalStateException;
import java.net.ConnectException;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 */

@Component
@Lazy
@Configurable(dependencyCheck = true, preConstruction = true)
public class DefaultPaymentSession implements PaymentSession {
    private final String paymentSessionId;
    @Autowired
    private SeapayGatewayWebService seapayGatewayWebService;
    private Logger logger = LoggerFactory.getLogger(DefaultPaymentSession.class);

    @Autowired
    public DefaultPaymentSession(@Value("${merchant.merchantId}") String merchantId, @Value("${merchant.serviceId}") String serviceId, @Value("${merchant.password}") String password) {
        hasText(merchantId, "MerchantId should not be null or empty");
        hasText(serviceId, "ServiceId should not be null or empty");
        hasText(password, "Password should not be null or empty");

        //todo make session creation lazy
        this.paymentSessionId = seapayGatewayWebService.createSession(merchantId, serviceId, password);
        logger.info("Seapay payment session created: sessionId is: {}", paymentSessionId);
    }

    @Override
    public String createTransaction(int amount, String description, String callbackUrl, String customerId) {
        isTrue(amount > 0, "Amount should be positive.");
        hasText(description, "Description should have text and can not be null");
        hasText(callbackUrl, "Callback url should have text and can not be null");
        try {
            final String createdTransactionId = seapayGatewayWebService.createTransaction(paymentSessionId, amount, description, callbackUrl, customerId);
            return createdTransactionId;
        } catch (WebServiceException e) {
            if (e.getCause() instanceof ConnectException)
                throw new CommunicationException(e);
            else
                throw e;
        }
    }

    @Override
    public void commitTransaction(String transactionId, int amount) {
        hasText(transactionId, "transactionId should not be null or empty");
        isTrue(amount > 0, "Amount should be positive.");
        try {
            seapayGatewayWebService.commitTransaction(paymentSessionId, transactionId, amount);
        } catch (WebServiceException e) {
            if (e.getCause() instanceof ConnectException)
                throw new CommunicationException(e);
            else
                throw e;
        } catch (AuthenticationException_Exception e) {
            throw new AuthenticationException(e);
        } catch (IllegalStateException_Exception e) {
            throw new IllegalStateException(e);
        } catch (CommunicationException_Exception e) {
            throw new CommunicationException(e);
        } catch (NotFoundException_Exception e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public TransactionStatus getTransactionStatus(String transactionId) {
        try {
            return seapayGatewayWebService.getTransactionStatus(paymentSessionId, transactionId);
        } catch (NotFoundException_Exception e) {
            throw new NotFoundException(e);
        } catch (AuthenticationException_Exception e) {
            throw new AuthenticationException(e);
        }
    }

    @Override
    public String getBankName(String transactionId) {
        try {
            return seapayGatewayWebService.getBankName(paymentSessionId, transactionId);
        } catch (NotFoundException_Exception e) {
            throw new NotFoundException(e);
        } catch (AuthenticationException_Exception e) {
            throw new AuthenticationException(e);
        }
    }
}