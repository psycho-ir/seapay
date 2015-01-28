package com.samenea.seapay.web.controller;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.model.BankTransactionInfoRepository;
import com.samenea.seapay.bank.model.BankTransactionService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author: Soroosh Sarabadani
 * Date: 9/9/13
 * Time: 11:16 AM
 */
@Controller
public class TransactionUnstableController {
    @Autowired
    BankTransactionInfoRepository bankTransactionInfoRepository;
    @Autowired
    BankTransactionService bankTransactionService;
    @Autowired
    ITransactionService transactionService;
    private Logger logger = LoggerFactory.getLogger(TransactionUnstableController.class);


    @RequestMapping(value = "/admin/findUnstables", method = {RequestMethod.GET})
    public String findAllUnstables(ModelMap modelMap) {
        logger.info("Finding unstable transactions started");
        final List<? extends ITransaction> allFailedTransactions = transactionService.findTransactionsByStatus(TransactionStatus.FAILED);
        logger.info("Number of candidate transactions: {}  ", allFailedTransactions.size());
        for (ITransaction transaction : allFailedTransactions) {
            final boolean b = bankTransactionService.pollTransaction(transaction);
            if (b) logger.info("Transaction: {} is not stable.", transaction.getTransactionId());

        }
        return "transaction/create";
    }
}
