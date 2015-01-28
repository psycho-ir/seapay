
package com.samenea.seapay.web.controller;

import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.commons.webmvc.controller.BaseController;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.web.model.TransactionViewModel;
import com.samenea.seapay.web.model.View;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * User: soroosh
 * Date: 11/28/12
 * Time: 11:56 AM
 * The responsibility of this controller is finding premitted banks for created transaction.
 */
@Controller
public class BankSelectionController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BankSelectionController.class);
    public static final String SPRING_REDIRECT_PREFIX = "redirect:";
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    TrackingService trackingService;
    @Autowired
    SeaPayTrackingTranslator seaPayTrackingTranslator;


    /**
     * Merchant calls this action for her transaction.
     * client can see transaction info and select from permitted banks.
     */
    @RequestMapping("/banks/{transactionId}")
    public String findPermittedBanks(@PathVariable String transactionId, ModelMap modelMap) {
        Assert.hasText(transactionId, "TransactionId should not be empty or null");

        List<String> bankNames = merchantService.findBanks(transactionId);
        MDC.put("transactionId", transactionId);
        logger.info("Selecting permitted banks for transaction: {}", transactionId);
        trackingService.record(transactionId, seaPayTrackingTranslator.translate(SeaPayTrackingTranslator.DISPLAYED_BANK_LIST, StringUtils.join(bankNames, ", ")));
        final IOrder order = orderService.findOrderByTransactionId(transactionId);
        final ITransaction transaction = transactionService.getTransaction(transactionId);
        TransactionViewModel transactionViewModel = new TransactionViewModel(transaction.getAmount(), transactionId, transaction.getDescription());
        modelMap.addAttribute("banks", bankNames);
        modelMap.addAttribute("order", order);
        modelMap.addAttribute("callbackUrl", order.getCallbackUrl());
        modelMap.addAttribute("transactionViewModel", transactionViewModel);
        modelMap.addAttribute("transaction", transaction);
        modelMap.addAttribute("transactionId", transactionId);
        logger.debug("Selectable banks are :" + bankNames.toString());
        //todo order information (merchant,OrderId,amount) $reveiew2
        return View.Bank.PERMITTED_BANKS;
    }

    @RequestMapping(value = "/transaction/cancel/{transactionId}")
    public String cancelTransaction(@PathVariable final String transactionId) {
        Assert.hasText(transactionId, "TransactionId should not be empty or null");
        final ITransaction transaction = transactionService.getTransaction(transactionId);
        logger.info("User clicked on back! Transaction: {}, Status: {}", transaction.getTransactionId(), transaction.getStatus());
        if (transaction.getStatus() != TransactionStatus.BANK_RESOLVED) {
            transaction.cancel();
        }

        final IOrder order = orderService.findOrderByTransactionId(transactionId);
        return SPRING_REDIRECT_PREFIX + order.getCallbackUrl();
    }

}