package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransactionStatus;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.repository.UnknowTransactionRepository;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Scheduler for settling not settled transactions in mellat gateway.
 *
 * @author: Soroosh Sarabadani
 * Date: 1/14/13
 * Time: 12:47 PM
 */
@Service
public class SettlerScheduler {
    private static final Logger logger = LoggerFactory.getLogger(SettlerScheduler.class);
    private static final Logger exceptionLogger = LoggerFactory.getLogger(SettlerScheduler.class, LoggerFactory.LoggerType.EXCEPTION);
    @Autowired
    private UnknowTransactionRepository unknowTransactionRepository;
    @Value("${mellat.settle.delayTime}")
    private String delayTime;

    @Transactional
    public void run() {
        List<UnknownTransaction> transactions = unknowTransactionRepository.findByStatus(UnknownTransactionStatus.SETTLE_PROBLEM);
        if (transactions == null || transactions.size() == 0) {
            logger.info("There is no unsettled transaction Mellat Gateway");
        }

        for (UnknownTransaction transaction : transactions) {
            try {
                MDC.put("transactionId", transaction.getTransactionId());
                transaction.investigate();
            } catch (Exception e) {
                logger.warn(e.getMessage());
                exceptionLogger.warn("Error in Settle Scheduler", e);

            } finally {

                logger.info("Transaction with id:{} was tried to settling. new status:{}", transaction.getTransactionId(), transaction.getStatus());
                MDC.remove("transactionId");
            }
        }
        logger.info("Settler Scheduler will run after {} seconds.", delayTime);
    }
}
