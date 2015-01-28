package com.samenea.seapay.bank.service.gateway.plugin.stub;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.PaymentResponseCode;
import com.samenea.seapay.bank.service.gateway.plugin.AbstractGatewayPlugin;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Soroosh Sarabadani
 * Date: 2/2/13
 * Time: 6:20 PM
 */

@Service
public class StubGatewayPlugin extends AbstractGatewayPlugin {
    private Logger logger = LoggerFactory.getLogger(StubGatewayPlugin.class);
    @Value("${seapay.stubBankUrl}")
    private String stubBankUrl = "http://localhost:8181/web/stub/request";

    @Override
    public IRedirectData request(TransactionInfo transaction) {
        return RedirectData.createForPost(stubBankUrl + "/" + transaction.getTransactionId(), new HashMap<String, String>());
    }

    @Override
    public boolean verify(TransactionInfo transaction, Map<String, String> responseParameters) {
        final String status = responseParameters.get("Status");
        if (status.equals("OK")) {
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponseCode interpretResponse(TransactionInfo transactionInfo, Map<String, String> parameters) {
        final String status = parameters.get("Status");
        if (status.equals("OK")) {
            return PaymentResponseCode.PAYMENT_OK;
        }
        return PaymentResponseCode.CANCELED_BY_USER;
    }

    @Override
    public boolean isCommited(TransactionInfo transactionInfo, BankTransactionInfo bankTransactionInfo) {
        final String status = bankTransactionInfo.getTransactionStartParams().get("Status");
        if (status.equals("OK")) {
            return true;
        }
        return false;
    }

    @Override
    public String getReferenceCodeFromResponse(Map<String, String> parameters) {
        return "";
    }
}
