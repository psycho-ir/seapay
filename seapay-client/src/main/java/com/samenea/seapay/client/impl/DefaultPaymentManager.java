package com.samenea.seapay.client.impl;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.seapay.client.DoubleSpendingException;
import com.samenea.seapay.client.OrderService;
import com.samenea.seapay.client.PaymentManager;
import com.samenea.seapay.client.PaymentSession;
import com.samenea.seapay.client.translation.ClientTrackingTranslator;
import com.samenea.seapay.client.ws.TransactionStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.samenea.seapay.client.ws.TransactionStatus.*;
import static org.springframework.util.Assert.hasText;

/**
 * @author: Jalal Ashrafi
 * Date: 1/17/13
 */
@Component
@Lazy
public class DefaultPaymentManager implements PaymentManager {
    @Autowired
    PaymentSession paymentSession;

    @Autowired
    OrderService orderService;
    private Logger logger = LoggerFactory.getLogger(DefaultPaymentManager.class);
    private Logger exceptionLogger = LoggerFactory.getLogger(DefaultPaymentManager.class, LoggerFactory.LoggerType.EXCEPTION);

    @Autowired
    TrackingService trackingService;

    @Autowired
    ClientTrackingTranslator clientTrackingTranslator;

    @Value("${merchant.callbackUrl}")
    private String callbackUrl;

    @Transactional
    @Override
    public String startTransaction(String orderId, String customerId) {
        hasText(orderId , "orderId should not be null or Empty");
        final int amount = orderService.orderAmount(orderId);
        final String transactionId = paymentSession.createTransaction(amount, "nodesc", buildCallBackUrl(orderId), customerId);
        logger.debug("Transaction with transactionId is: {} and amount: {} is created on seapay.",transactionId,amount );
        orderService.assignTransaction(orderId, transactionId);
        logger.debug("Transaction with transactionId is: {} assigned to orderId: {}", transactionId, orderId);
        return transactionId;
    }

    private String buildCallBackUrl(String orderId) {
        return callbackUrl+orderId;
    }

    @Transactional(noRollbackFor = CommunicationException.class)
    @Override
    public void processTransaction(String orderId, String transactionId) {
        final int amount = orderService.orderAmount(orderId);
        try {
            orderService.checkDoubleSpending(orderId,transactionId);
        } catch (DoubleSpendingException e) {
            orderService.cancel(orderId);
            throw e;
        }
        try {
            final TransactionStatus transactionStatus = paymentSession.getTransactionStatus(transactionId);
            final String bankName = paymentSession.getBankName(transactionId);
            if (transactionStatus == FAILED){
                logger.warn("Status Transaction with transactionId '{}' is: '{}'. Now canceling order: '{}'. . .", transactionId, transactionStatus, orderId);
                orderService.cancel(orderId);
                logger.info("Order: '{}' canceled due invalid transaction status", orderId);
                trackingService.record(orderId, clientTrackingTranslator.translate(ClientTrackingTranslator.TRANSACTION_STATUS_PROBLEM, transactionId));
                throw new IllegalStateException(String.format("Transaction with transactionId: %s has status: %s",transactionId,transactionStatus));
            }
            commitIfNeeded(orderId, transactionId, amount, transactionStatus);
            orderService.deliver(orderId,transactionId, bankName);

            logger.debug("Order delivered successfully: '{}'", orderId.toString());
        } catch (CommunicationException e) {
            logger.warn("Commit Transaction status is not known now, it should be tried again later. {}",e.getMessage());
            trackingService.record(orderId,clientTrackingTranslator.translate(ClientTrackingTranslator.CONNECTION_SEAPAY_PROBLEM));
            exceptionLogger.warn("Can not communicate to payment system.", e);
            orderService.postponeDelivery(orderId,transactionId);
            trackingService.record(orderId, String.format("order was postponed with trasaction by id : %s", transactionId));
            throw e;
        }
    }

    private void commitIfNeeded(String orderId, String transactionId, int amount, TransactionStatus transactionStatus) {
        if (isCommitted(transactionStatus)){
            logger.debug("Transaction with transactionId is: '{}' and amount: '{}' is ALREADY committed on seapay. now delivering order", transactionId, amount);
            trackingService.record(transactionId, clientTrackingTranslator.translate(ClientTrackingTranslator.TRANSACTION_ALREADY_COMMITTED, orderId));

        }else{
            paymentSession.commitTransaction(transactionId, amount);
            trackingService.record(transactionId, clientTrackingTranslator.translate(ClientTrackingTranslator.TRANSACTION_SUCCESSFULLY_COMMITTED, orderId));
            logger.debug("Transaction with transactionId is: '{}' and amount: '{}' is committed on seapay. now delivering order", transactionId, amount);
        }
    }

    private boolean isCommitted(TransactionStatus transactionStatus) {
        return transactionStatus == COMMITED || transactionStatus == COMMITED_WITH_DELAY;
    }

}
