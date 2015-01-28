package com.samenea.seapay.client.translation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ngs
 * Date: 3/27/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ClientTrackingTranslator {

    public static final String START_TRANSACTION = "START_TRANSACTION";
    public static final String REDIRECT_TO_CALLBACKURL = "REDIRECT_TO_CALLBACKURL";
    public static final String CONNECTION_SEAPAY_PROBLEM = "CONNECTION_SEAPAY_PROBLEM";
    public static final String TRANSACTION_STATUS_PROBLEM = "TRANSACTION_STATUS_PROBLEM";
    public static final String TRANSACTION_ALREADY_COMMITTED = "TRANSACTION_ALREADY_COMMITTED";
    public static final String TRANSACTION_SUCCESSFULLY_COMMITTED = "TRANSACTION_SUCCESSFULLY_COMMITTED";

    @Value("${transaction.start}")
    String transactionStarted;

    @Value("${redirect.to.callbackurl}")
    String redirectToCallBackUrl;

    @Value("${connection.seapay.problem}")
    String connectionSeapayProblem;

    @Value("${transaction.status.problem}")
    String transactionStatusProblem;

    @Value("${transaction.already.committed}")
    String transactionAlreadyCommitted;

    @Value("${transaction.successfully.committed}")
    String transactionSuccessfullyCommitted;

    Map<String, String> allMessages;

    public void createMessages(){
        allMessages = new HashMap<String, String>();
        Assert.notNull(transactionStarted);
        Assert.notNull(redirectToCallBackUrl);
        Assert.notNull(connectionSeapayProblem);
        Assert.notNull(transactionStatusProblem);
        Assert.notNull(transactionAlreadyCommitted);
        Assert.notNull(transactionSuccessfullyCommitted);

        allMessages.put(START_TRANSACTION, transactionStarted);
        allMessages.put(REDIRECT_TO_CALLBACKURL, redirectToCallBackUrl);
        allMessages.put(CONNECTION_SEAPAY_PROBLEM, connectionSeapayProblem);
        allMessages.put(TRANSACTION_STATUS_PROBLEM, transactionStatusProblem);
        allMessages.put(TRANSACTION_ALREADY_COMMITTED, transactionAlreadyCommitted);
        allMessages.put(TRANSACTION_SUCCESSFULLY_COMMITTED, transactionSuccessfullyCommitted);
    }

    public String translate(String messageCode, String... args){
        if(allMessages == null){
            createMessages();
        }
        return String.format(allMessages.get(messageCode), args);
    }
}
