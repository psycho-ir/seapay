package com.samenea.seapay.bank.service.gateway.plugin;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.GatewayPlugin;
import com.samenea.seapay.bank.model.BankAccount;
import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;
import com.samenea.seapay.bank.service.BankAccountService;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Common part of gateway plugins are here,
 * every gateway plugin must extends AbstractGatewayPlugin and implement abstract methods.
 *
 * @author: Soroosh Sarabadani
 * Date: 1/27/13
 * Time: 2:58 PM
 */

public abstract class AbstractGatewayPlugin implements GatewayPlugin {
    @Autowired
    protected Environment environment;

    @Autowired
    protected BankAccountService bankAccountService;
    @Value("${seapay.callbackUrl}")
    private String callbackUrl = "http://localhost:8181/web/transaction/verification/TRN-100";

    protected final PaymentGatewayConfiguration getPaymentGatewayConfiguration(TransactionInfo transactionInfo) {
        final BankAccount bankAccount = bankAccountService.findAccount(transactionInfo.getAccountNumber());
        final PaymentGatewayConfiguration paymentGatewayConfiguration = bankAccount.getPaymentGatewayConfiguration();
        return paymentGatewayConfiguration;
    }

    protected final String getCallback(TransactionInfo transactionInfo) {
        return callbackUrl.replace("{transactionId}", transactionInfo.getTransactionId());
    }
}
