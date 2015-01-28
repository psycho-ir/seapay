package com.samenea.seapay.bank.service.gateway.plugin.mellat.model;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.model.BankAccount;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.service.BankAccountService;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.MellatGatewayConfigurationReader;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.MellatResponseParams;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/9/13
 * Time: 12:42 PM
 */

@Configurable(preConstruction = true, dependencyCheck = true)
public class SettleProblemInvestigator {
    private static final Logger logger = LoggerFactory.getLogger(SettleProblemInvestigator.class);
    private final BankTransactionInfo bankTransactionInfo;
    private final MellatResponseParams mellatResponseParams;
    private final MellatGatewayConfigurationReader mellatGatewayConfigurationReader;

    @Autowired(required = true)
    private BankAccountService bankAccountService;
    @Autowired
    private MellatWSWrapper mellatWSWrapper;


    public SettleProblemInvestigator(BankTransactionInfo bankTransactionInfo, String accountNumber) {
        this.bankTransactionInfo = bankTransactionInfo;
        MDC.put("transactionId", this.bankTransactionInfo.getTransactionId());
        this.mellatResponseParams = new MellatResponseParams(bankTransactionInfo.getPaymentResponseParams());
        BankAccount bankAccount = bankAccountService.findAccount(accountNumber);
        this.mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(bankAccount.getPaymentGatewayConfiguration());

    }

    public void investigate() {

        final Long saleOrderId = this.mellatResponseParams.getSaleOrderId();
        final Long saleReferenceId = mellatResponseParams.getSaleReferenceId();
        MellatWSResponse mellatWSResponse = mellatWSWrapper.settle(this.mellatGatewayConfigurationReader.getAuthenticationData(), saleOrderId, saleOrderId, saleReferenceId);
        if (mellatWSResponse.isOK() || mellatWSResponse.isSettleBefore()) {
            return;
        } else {
            throw new VerifyException(String.format("Mellat investigation has problem for Transaction:%s Result Code:%s ", bankTransactionInfo.getTransactionId(), mellatWSResponse.getResultCode()));
        }
    }
}
