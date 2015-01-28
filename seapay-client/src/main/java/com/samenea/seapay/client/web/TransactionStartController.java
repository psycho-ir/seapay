package com.samenea.seapay.client.web;

import com.samenea.commons.component.utils.UserContext;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.commons.webmvc.controller.BaseController;
import com.samenea.commons.webmvc.model.MessageType;
import com.samenea.seapay.client.PaymentManager;
import com.samenea.seapay.client.impl.CommunicationException;
import com.samenea.seapay.client.translation.ClientTrackingTranslator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 */
@Controller
@Lazy
public class TransactionStartController extends BaseController{
    public static final String SPRING_REDIRECT_PREFIX = "redirect:";
    public static final String DEFAULT_USER_NAME = "Unknown";
    //region Loggers
    private Logger logger = LoggerFactory.getLogger(TransactionStartController.class);
    private Logger exceptionLogger = LoggerFactory.getLogger(TransactionStartController.class, LoggerFactory.LoggerType.EXCEPTION);
    //endregion
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    @Qualifier("environment")
    UserContext userContext;
    @Autowired
    @Value("${seapay.seapayUrl}")
    String seapayUrl;
    @Autowired
    TrackingService trackingService;
    @Autowired
    ClientTrackingTranslator trackingTranslator;

    @RequestMapping(value = "/payment/startTransaction/{orderId}/")
    public String startTransaction(@PathVariable String orderId) {
         //should be replaced when spring security is added
        logger.debug("About to start transaction for orderId: {}",orderId);
        final String transactionId;
        try {
            transactionId = paymentManager.startTransaction(orderId, getCustomerId());
            trackingService.makeSynonym(orderId,transactionId);
            trackingService.record(orderId, trackingTranslator.translate(ClientTrackingTranslator.START_TRANSACTION, transactionId));
            final String processTransactionCallbackUrl = buildProcessTransactionUrl(transactionId);
            logger.debug("redirecting to: {}", processTransactionCallbackUrl);
            trackingService.record(orderId, trackingTranslator.translate(ClientTrackingTranslator.REDIRECT_TO_CALLBACKURL, SPRING_REDIRECT_PREFIX + processTransactionCallbackUrl));
            return SPRING_REDIRECT_PREFIX + processTransactionCallbackUrl;
        } catch (CommunicationException e) {
            trackingService.record(orderId,trackingTranslator.translate(ClientTrackingTranslator.CONNECTION_SEAPAY_PROBLEM));
            logger.warn("Could not connect to seapay. {}",e.getMessage());
            exceptionLogger.warn("Could not connect to seapay.",e);
            addMessage("Currently can not connect to seapay. Please try again later.", MessageType.ERROR);
            return "/confirm/"+orderId;
        }
    }

    private String getCustomerId() {
        final Principal principal = userContext.currentUser();
        String customerId = DEFAULT_USER_NAME;
        if(principal != null) {
            customerId = principal.getName();
        }
        return customerId;
    }


    private String buildProcessTransactionUrl(String transactionId) {
        return seapayUrl + transactionId;
    }
}
