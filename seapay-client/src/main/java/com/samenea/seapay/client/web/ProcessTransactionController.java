package com.samenea.seapay.client.web;

import com.samenea.seapay.client.PaymentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 */
@Controller
@Lazy
public class ProcessTransactionController {
    public static final String TRANSACTION_SUCCESS = "PAYMENT_OK";
    private Logger logger = LoggerFactory.getLogger(ProcessTransactionController.class);
    @Autowired
    PaymentManager paymentManager;
    @RequestMapping("/payment/processTransactionResult/{orderId}")
    public String process(String transactionId,@PathVariable String orderId, String result) {
        logger.debug("Processing transaction {} for orderId: {} reported result is {}",transactionId,orderId,result);
        if(TRANSACTION_SUCCESS.equalsIgnoreCase(result)){
            paymentManager.processTransaction(orderId,transactionId);
            logger.info("Transaction {} processed for orderId: {}", transactionId, orderId);
            return "SUCCESS";
        }else{
            logger.debug("Reported transaction result is: {} for transaction: {}",result,transactionId);
            return "FAILED";
        }

    }
}
