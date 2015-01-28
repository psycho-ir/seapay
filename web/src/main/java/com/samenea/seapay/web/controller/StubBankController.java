package com.samenea.seapay.web.controller;

import com.samenea.commons.component.utils.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: Soroosh Sarabadani
 * Date: 2/2/13
 * Time: 6:42 PM
 */
@Controller
public class StubBankController {
    private Logger logger = LoggerFactory.getLogger(StubBankController.class);

    @RequestMapping("/stub/request/{transactionId}")
    public String request(@PathVariable String transactionId, ModelMap modelMap) {
        modelMap.addAttribute("transactionId", transactionId);
        return "bank/stub";
    }
}
