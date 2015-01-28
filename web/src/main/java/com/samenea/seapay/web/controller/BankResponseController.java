package com.samenea.seapay.web.controller;

import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.commons.webmvc.controller.BaseController;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.web.model.View;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 11/28/12
 * Time: 11:56 AM
 * The responsibility of this controller is interpreting bank responses and  sending suitable result to merchant.
 */
@Controller
public class BankResponseController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BankResponseController.class);
    @Autowired
    private IBankTransactionService bankService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    TrackingService trackingService;
    @Autowired
    SeaPayTrackingTranslator seaPayTrackingTranslator;

    /**
     * This action will call from gateways. In this action we interpret the answer of bank
     * and then send suitable result to merchant callbackurl.
     */
    @RequestMapping(value = "/bank/{transactionId}/interpret", method = {RequestMethod.POST, RequestMethod.GET})
    public String interpret(ModelMap modelMap, HttpServletRequest request, @PathVariable String transactionId) {
        trackingService.record(transactionId, seaPayTrackingTranslator.translate(SeaPayTrackingTranslator.BANK_RESPONSE_RECEIVED));
        MDC.put("transactionId", transactionId);
        logger.info("returned from bank payment page");
        Map<String, String> parameters = createParameters(request);
        logger.debug("Returned bank response params: {}", parameters.toString());
        final ITransaction transaction = transactionService.getTransaction(transactionId);

        String result;
        try {
            result = bankService.interpretPaymentResponse(transaction, parameters).toString();
        } catch (Exception e) {
            logger.info("Stale event occurred for Transaction:{}.", e.getClass());
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e1) {
                logger.error("Inerrupt exception in sleeping.", e1);
            }
            result = bankService.interpretPaymentResponse(transaction, parameters).toString();
        }

        logger.info("Bank response interpret result is : {},{}", transactionId, result);
        final IOrder order = orderService.findOrderByTransactionId(transactionId);

        if (result.equals("CANCELED_BY_USER")){
            transaction.cancel();
        }
        modelMap.addAttribute("result", result);

        modelMap.addAttribute("order", order);
        if (parameters.keySet().contains("SaleReferenceId")) {
            modelMap.addAttribute("BANK_REFERENCE_ID", parameters.get("SaleReferenceId"));
        } else {
            modelMap.addAttribute("BANK_REFERENCE_ID", "");
        }
        trackingService.record(transactionId, seaPayTrackingTranslator.translate(SeaPayTrackingTranslator.REDIRECT_TO_MERCHANT));
        logger.debug("redirecting to {}", View.Merchant.REDIRECT);
        return View.Merchant.REDIRECT;
    }

    private Map<String, String> createParameters(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        final Map<String, String> parameters = new HashMap<String, String>();
        for (String key : requestParameterMap.keySet()) {
            parameters.put(key, requestParameterMap.get(key)[0]);
        }
        return parameters;
    }
}

