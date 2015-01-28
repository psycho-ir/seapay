package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionService;
import com.samenea.seapay.transaction.*;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.jws.WebService;

/**
 * @author: Soroosh Sarabadani
 * Date: 12/30/12
 * Time: 12:50 PM
 */

@WebService(serviceName = "seapayGatewayService", endpointInterface = "com.samenea.seapay.transaction.ISeapayGatewayService")
@Service("seapayGatewayService")
public class SeapayGatewayService implements ISeapayGatewayService {
    //region Loggers
    private static final Logger logger = LoggerFactory.getLogger(SeapayGatewayService.class);
    private static final Logger exceptionLogger = LoggerFactory.getLogger(SeapayGatewayService.class, LoggerFactory.LoggerType.EXCEPTION);
    //endregion
    @Qualifier("orderService")
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ISessionService sessionService;

    @Autowired
    private ITransactionService transactionService;

    @Override
    public String createSession(String merchantId, String serviceId, String password) {
        logger.info("merchant with id:{} and service id:{} requested for creating session.", merchantId, serviceId);
        return this.sessionService.createSession(merchantId, serviceId, password).getSessionId();
    }

    @Override
    public String createTransaction(String sessionId, int amount, String description, String callbackUrl, String customerId) {
        final ISession session = this.sessionService.getSession(sessionId);
        return session.createTransaction(amount, description, callbackUrl, customerId).getTransactionId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void commitTransaction(String sessionId, String transactionId, int amount) {
        Assert.hasText(transactionId, "transactionId can not be empty or null");
        MDC.put("transactionId", transactionId);
        checkAuthentication(sessionId, transactionId);
        ISession session = sessionService.getSession(sessionId);
        logger.debug("merchant called commit for transaction:{} amount{} ", transactionId, amount);

        try {
            session.commit(transactionId, amount);
        } catch (IllegalStateException e) {
            throw new com.samenea.seapay.transaction.exceptions.IllegalStateException(e);
        }
    }

    @Override
    public TransactionStatus getTransactionStatus(String sessionId, String transactionId) {
        Assert.hasText(transactionId, "transactionId can not be empty or null");
        checkAuthentication(sessionId, transactionId);
        final ITransaction transaction = transactionService.getTransaction(transactionId);
        return transaction.getStatus();
    }

    @Override
    public String getBankName(String sessionId, String transactionId) throws AuthenticationException, NotFoundException {
        Assert.hasText(transactionId, "transactionId can not be empty or null");
        checkAuthentication(sessionId, transactionId);
        final ITransaction transaction = transactionService.getTransaction(transactionId);
        return transaction.getBankName();

    }

    //region private methods
    private void checkAuthentication(String sessionId, String transactionId) {
        final ISession session;
        try {
            session = sessionService.getSession(sessionId);
        } catch (NotFoundException e) {
            logger.warn("No session found with this sessionID: {}", sessionId);
            exceptionLogger.warn(String.format("No session found with this sessionID: %s", sessionId), e);
            throw new AuthenticationException(e);
        }
        final IOrder order = orderService.findOrderByTransactionId(transactionId);
        if (!order.checkIsFor(transactionId, session.getMerchantId(), session.getServiceId())) {
            throw new AuthenticationException(String.format("merchant: %s can not access transaction: %s for service %s"
                    , session.getMerchantId(), transactionId, session.getServiceId()));
        }
    }
    //endregion

}

