package com.samenea.seapay.web.translation;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ngs
 * Date: 3/27/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Configurable(preConstruction = true, dependencyCheck = true)
@Service
public class SeaPayTrackingTranslator {

    public static final String BANK_RESPONSE_RECEIVED = "BANK_RESPONSE_RECEIVED";
    public static final String REDIRECT_TO_MERCHANT = "REDIRECT_TO_MERCHANT";
    public static final String DISPLAYED_BANK_LIST = "DISPLAYED_BANK_LIST";
    public static final String USER_SELECTED_BANK = "USER_SELECTED_BANK";
    public static final String REDIRECT_TO_BANK = "REDIRECT_TO_BANK";


    @Value("${bank.response.received}")
    String bankResponseReceived;

    @Value("${to.merchant.redirect}")
    String redirectToMerchant;

    @Value("${displayed.bank.list}")
    String displayedBankList;

    @Value("${user.selected.bank}")
    String userSelectedBank;

    @Value("${to.bank.redirect}")
    String toBankRedirect;

    Map<String, String> allMessages;

    public void createMessages(){
        allMessages = new HashMap<String, String>();
        Assert.notNull(bankResponseReceived);
        Assert.notNull(redirectToMerchant);
        Assert.notNull(displayedBankList);
        Assert.notNull(userSelectedBank);
        Assert.notNull(toBankRedirect);

        allMessages.put(BANK_RESPONSE_RECEIVED, bankResponseReceived);
        allMessages.put(REDIRECT_TO_MERCHANT, redirectToMerchant);
        allMessages.put(DISPLAYED_BANK_LIST, displayedBankList);
        allMessages.put(USER_SELECTED_BANK, userSelectedBank);
        allMessages.put(REDIRECT_TO_BANK, toBankRedirect);

    }

    public String translate(String messageCode, String... args){
        if(allMessages == null){
            createMessages();
        }
        return String.format(allMessages.get(messageCode), args);
    }
}
