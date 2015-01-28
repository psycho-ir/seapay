package com.samenea.seapay.web.controller;

import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.commons.webmvc.controller.BaseController;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.merchant.IService;
import com.samenea.seapay.web.model.View;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class StartTransactionController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BankSelectionController.class);

    @Autowired
     private IOrderService orderService;
    @Autowired
    private IBankTransactionService bankService;
    @Autowired
    TrackingService trackingService;
    @Autowired
    SeaPayTrackingTranslator seaPayTrackingTranslator;

    @RequestMapping("/banks/gateway/{bankName}/{transactionId}")
    public String start(ModelMap modelMap, @PathVariable final String bankName, @PathVariable final String transactionId) {
        trackingService.record(transactionId, seaPayTrackingTranslator.translate(SeaPayTrackingTranslator.USER_SELECTED_BANK, bankName));
//        todo should handle CommunicationException $review4
        Assert.hasText(transactionId,"transactionId should not be empty or null");
        Assert.hasText(bankName,"bankName should not be empty or null");
        MDC.put("transactionId", transactionId);
        logger.info("start transaction: {} on bank {}",transactionId,bankName);
        IOrder order = orderService.findOrderByTransactionId(transactionId);

        IService service = orderService.findServiceOfTransaction(transactionId);
        IBankAccount account = service.getAccount(bankName);
        final IRedirectData redirectData = bankService.startTransaction(transactionId, account);
        logger.info("{} bank transaction started.now redirecting on bank for transactionId : {}",bankName,transactionId);
        logger.debug("redirect data for transaction: {} is: {}", transactionId, redirectData.toString());
        createModelMap(modelMap, redirectData, order);
        trackingService.record(transactionId, seaPayTrackingTranslator.translate(SeaPayTrackingTranslator.REDIRECT_TO_BANK, redirectData.getUrl()));
        return View.Bank.REDIRECT;
    }

    private void createModelMap(ModelMap modelMap, IRedirectData redirectData,IOrder order) {
        modelMap.addAttribute("parameters", redirectData.getParameters());
        modelMap.addAttribute("order",order);
        modelMap.addAttribute("url", redirectData.getUrl());
        modelMap.addAttribute("method", redirectData.getHttpMethod());

    }
}
