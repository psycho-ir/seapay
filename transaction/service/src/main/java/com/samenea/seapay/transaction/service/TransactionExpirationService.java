package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Periodically will cancel transactions with status new
 * @author: Jalal Ashrafi
 * Date: 10/13/13
 */
@Service
public class TransactionExpirationService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionExpirationService.class);
    private static final Logger exceptionLogger = LoggerFactory.getLogger(TransactionExpirationService.class, LoggerFactory.LoggerType.EXCEPTION);

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private Environment environment;


    @Value("${transaction.expiration.timeoutMinutes}")
    private Integer expirationTimeoutMinutes = 15;
    @Scheduled(fixedDelay = 60000)
    public void run(){
        List<? extends ITransaction> newTransactions = transactionService.findTransactionsByStatus(TransactionStatus.NEW);
        for (ITransaction newTransaction : newTransactions) {
            if(expired(newTransaction)) {
                try {
                    newTransaction.cancel();
                    logger.info("Transaction expired {}",newTransaction.toString());
                } catch (Exception e) {
                    logger.warn("Error canceling transaction {}." +
                            " Error message is: {}. it will be retried {} minutes later.",
                            newTransaction.toString(), e.getMessage(), expirationTimeoutMinutes);
                    exceptionLogger.warn("Error canceling transaction {}.",newTransaction.toString(),e);

                }
            }
        }

    }

    private boolean expired(ITransaction newTransaction) {
        return newTransaction.getLastUpdateDate().getTime() + expirationTimeoutMinutes * 60 * 1000 < environment.getCurrentDate().getTime();
    }
}
