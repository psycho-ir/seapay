package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.Transaction;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/9/13
 * Time: 4:03 PM
 */

@Service
public class TransactionInvestigatorService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionInvestigatorService.class);
    private static final Logger exceptionLogger = LoggerFactory.getLogger(TransactionInvestigatorService.class, LoggerFactory.LoggerType.EXCEPTION);

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private Environment environment;
    @Value("${investigation.thresholdOfTransactionMinutes}")
    private String threshold = "15";
    @Value("${investigation.delayTime}")
    private String delayTime;

    private final InvestigationCandidate unknownInvestigationCandidate = new UnknownInvestigationCandidate();
    private final InvestigationCandidate bankResolvedInvestigationCandidate = new BankResolvedInvestigationCandidate();

    public void runInvestigation() {

        logger.info("Investigation scheduler started.");
        List<Transaction> unknownTransactions = (List<Transaction>) transactionService.findTransactionsByStatus(TransactionStatus.UNKNOWN);
        if (unknownTransactions == null || unknownTransactions.size() == 0) {
            logger.info("There is no Unknown Transaction for investigation.");
        }

        investigateTransactions(unknownTransactions, unknownInvestigationCandidate);

        List<Transaction> bankResolvedTransactions = (List<Transaction>) transactionService.findTransactionsByStatus(TransactionStatus.BANK_RESOLVED);
        if (bankResolvedTransactions == null || bankResolvedTransactions.size() == 0) {
            logger.info("There is no BANK_RESOLVED Transaction for investigation.");
        }
        investigateTransactions(bankResolvedTransactions, bankResolvedInvestigationCandidate);

        MDC.remove("transactionId");
        logger.info("Transaction investigation will run after {} seconds.", delayTime);
    }

    private void investigateTransactions(List<Transaction> unknownTransactions, InvestigationCandidate investigationCandidate) {
        for (Transaction transaction : unknownTransactions) {
            MDC.put("transactionId", transaction.getTransactionId());
            if (investigationCandidate.isCandidate(transaction)) {
                logger.info("Transaction with id {} is investigating.", transaction.getTransactionId());
                try {
                    transaction.investigate();
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    exceptionLogger.warn("Error in Transaction Investigator Service", e);

                }
                logger.info("Transaction with id {} investigated. Transaction Status after investigation:{}", transaction.getTransactionId(), transaction.getStatus());
            } else {
                logger.debug("Start date of Transaction with id:{} is not OK for investigation.", transaction.getTransactionId());
            }
        }
    }

    private interface InvestigationCandidate {
        boolean isCandidate(Transaction transaction);
    }

    private class UnknownInvestigationCandidate implements InvestigationCandidate {

        @Override
        public boolean isCandidate(Transaction transaction) {
            final long thresholdToMinutes = Long.valueOf(threshold) * 60 * 1000;
            return transaction.getStartDate().before(new Date(environment.getCurrentDate().getTime() - Long.valueOf(thresholdToMinutes)));

        }
    }

    private class BankResolvedInvestigationCandidate implements InvestigationCandidate {
        @Override
        public boolean isCandidate(Transaction transaction) {
            final long thresholdToMinutes = Long.valueOf(threshold) * 60 * 1000 * 2;
            return transaction.getStartDate().before(new Date(environment.getCurrentDate().getTime() - Long.valueOf(thresholdToMinutes)));

        }
    }
}
