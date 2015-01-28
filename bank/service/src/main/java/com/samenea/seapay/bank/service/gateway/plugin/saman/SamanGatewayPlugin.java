package com.samenea.seapay.bank.service.gateway.plugin.saman;

import com.samenea.commons.component.utils.command.MaxRetryReachedException;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.PaymentResponseCode;
import com.samenea.seapay.bank.service.gateway.plugin.AbstractGatewayPlugin;
import com.samenea.seapay.bank.service.gateway.plugin.saman.ws.SamanWSWrapper;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soroosh on 6/16/14.
 */

@Service
public class SamanGatewayPlugin extends AbstractGatewayPlugin {
    public static final String MID = "21016922";
    private final Logger logger = LoggerFactory.getLogger(SamanGatewayVerifier.class);

    @Autowired
    private SamanWSWrapper samanWSWrapper;

    @Override
    public IRedirectData request(TransactionInfo transaction) {
        Map<String, String> parameters = new HashMap<String, String>(6);
        parameters.put("Amount", String.valueOf(transaction.getAmount()));
        parameters.put("MID", "21016922");
        parameters.put("ResNum", String.valueOf(transaction.getTransactionNumber()));
        parameters.put("RedirectUrl", getCallback(transaction));

        IRedirectData redirectData = RedirectData.createForPost("https://sep.shaparak.ir/Payment.aspx", parameters);

        return redirectData;
    }

    @Override
    public boolean verify(TransactionInfo transaction, Map<String, String> responseParameters) {
        final boolean isSame = checkTransactionIsTheSame(transaction, Long.valueOf(responseParameters.get("ResNum")));
        if (!isSame) {
            return false;
        }
        if (!responseParameters.containsKey("RefNum")) {
            return false;
        }

        SamanGatewayVerifier verifier = new SamanGatewayVerifier(this.samanWSWrapper);
        try {
            verifier.verify(responseParameters.get("RefNum"), MID, transaction);
        } catch (MaxRetryReachedException ve) {
            return false;
        }

        logger.debug("Saman verify result is : OK for transaction:{}", transaction.getTransactionId());
        return true;
    }

    @Override
    public PaymentResponseCode interpretResponse(TransactionInfo transactionInfo, Map<String, String> parameters) {
        final boolean isSame = checkTransactionIsTheSame(transactionInfo, Long.valueOf(parameters.get("ResNum")));
        if (!isSame) {
            return PaymentResponseCode.CANCELED_BY_USER;
        }
        if (parameters.containsKey("State")) {
            final String result = parameters.get("State");
            if (result.toUpperCase().trim().equals("OK")) {
                return PaymentResponseCode.PAYMENT_OK;
            } else {
                return PaymentResponseCode.CANCELED_BY_USER;
            }
        } else {
            return PaymentResponseCode.CANCELED_BY_USER;
        }
    }

    @Override
    public boolean isCommited(TransactionInfo transactionInfo, BankTransactionInfo bankTransactionInfo) {
        if (!bankTransactionInfo.getPaymentResponseParams().containsKey("RefNum")) {
            return false;
        }
        boolean has_state = bankTransactionInfo.getPaymentResponseParams().containsKey("State");

        if (has_state && !bankTransactionInfo.getPaymentResponseParams().get("State").trim().toUpperCase().equals("OK")) {
            return false;
        }

        SamanGatewayVerifier verifier = new SamanGatewayVerifier(this.samanWSWrapper);
        try {
            verifier.verify(bankTransactionInfo.getPaymentResponseParams().get("RefNum"), MID, transactionInfo);
        } catch (VerifyException ve) {
            logger.error("Verify error occured: " + ve.getMessage());
            return false;
        }  catch (MaxRetryReachedException ve) {
            return false;
        }

        logger.debug("Saman verify result is : OK for transaction:{}", transactionInfo.getTransactionId());
        return true;
    }

    @Override
    public String getReferenceCodeFromResponse(Map<String, String> parameters) {
        if (parameters.containsKey("RefNum")) {
            return parameters.get("RefNum");
        } else {
            return "";
        }
    }

    private boolean checkTransactionIsTheSame(TransactionInfo transactionInfo, Long orderId) {
        return transactionInfo.getTransactionNumber() == orderId;
    }
}
