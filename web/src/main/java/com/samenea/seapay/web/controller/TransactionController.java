package com.samenea.seapay.web.controller;

import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.model.SessionFactory;
import com.samenea.seapay.transaction.ITransaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 11/28/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class TransactionController {


    @RequestMapping(value = "/transaction/create", method = {RequestMethod.GET})
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("transaction", new Trn());

        return "transaction/create";
    }

    @RequestMapping(value = "/transaction/create", method = {RequestMethod.POST})
    public String createPost(@ModelAttribute("transaction") @Valid Trn transaction, Errors errors, ModelMap modelMap) {
        if (errors.hasErrors()) {
            modelMap.addAttribute("transaction", transaction);
            return "transaction/create";
        }
        //todo: this API must be changed
        SessionFactory s = new SessionFactory();
        try {
            ISession session = s.createSession("M-100", "S-100", "123456");
            ITransaction t = session.createTransaction(transaction.getAmount(), transaction.getDescription(), "http://www.yahoo.com", "C-100");
            return "redirect:/banks/" + t.getTransactionId();

        } catch (RequestNotValidException e) {
            e.printStackTrace();
        }

        return "";
    }



    public static class Trn {
        private Integer amount = 0;
        private String description = "";

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

}
