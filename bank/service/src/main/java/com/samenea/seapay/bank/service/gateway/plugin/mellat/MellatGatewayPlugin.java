package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.command.MaxRetryReachedException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.model.*;
import com.samenea.seapay.bank.service.gateway.plugin.AbstractGatewayPlugin;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.repository.UnknowTransactionRepository;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.samenea.commons.component.utils.log.LoggerFactory.LoggerType.EXCEPTION;

@Service
public class MellatGatewayPlugin extends AbstractGatewayPlugin {
    private final Logger logger = LoggerFactory.getLogger(MellatGatewayPlugin.class);
    private final Logger exceptionLogger = LoggerFactory.getLogger(MellatGatewayPlugin.class, EXCEPTION);


    @Autowired
    private MellatWSWrapper mellatWSWrapper;

    @Autowired
    private UnknowTransactionRepository unknowTransactionRepository;

    @Autowired
    private BankTransactionInfoRepository bankTransactionInfoRepository;


    @Override
    public IRedirectData request(TransactionInfo transactionInfo) {

        final MellatGatewayConfigurationReader configReader = new MellatGatewayConfigurationReader(getPaymentGatewayConfiguration(transactionInfo));

        final String requestCallback = getCallback(transactionInfo);

        logger.debug("Mellat Webservice called auth: {}, OrderId: {}, transactionNumber: {}, Amount: {}, CallbackUrl: {}",
                configReader.getAuthenticationData().toString(), transactionInfo.getTransactionId(), transactionInfo.getTransactionNumber(),
                transactionInfo.getAmount(), requestCallback);

        MellatWSResponse mellatWSResponse = mellatWSWrapper.payRequest(configReader.getAuthenticationData(),
                transactionInfo.getTransactionNumber(), transactionInfo.getAmount(), requestCallback, "");

        return new RedirectDataBuilder(mellatWSResponse).createPaymentResponse();

    }

    @Override
    public boolean verify(final TransactionInfo transaction, final Map<String, String> parameters) {
        final MellatResponseParams mellatResponseParams = new MellatResponseParams(parameters);
        final boolean isSame = checkTransactionIsTheSame(transaction, mellatResponseParams.getSaleOrderId());
        if (!isSame) {
            return false;
        }
        final BankTransactionInfo bankTransactionInfo = bankTransactionInfoRepository.findByTransactionId(transaction.getTransactionId());
        bankTransactionInfo.setReferenceCode(mellatResponseParams.getSaleReferenceId().toString());
        bankTransactionInfoRepository.store(bankTransactionInfo);

        final MellatGatewayConfigurationReader configReader = new MellatGatewayConfigurationReader(getPaymentGatewayConfiguration(transaction));
        logger.debug("About to verify transactionId: {}", transaction.getTransactionId());
        final MellatGatewayVerifier gatewayVerifier = new MellatGatewayVerifier(mellatWSWrapper, configReader, mellatResponseParams);

        try {
            gatewayVerifier.verify();
        } catch (MaxRetryReachedException e) {
            saveUnknownTransaction(transaction);
            throw e;
        }

        logger.debug("Mellat verify result is : OK for transaction:{}",transaction.getTransactionId());
        try {
            logger.debug("About to settle transaction {},", transaction.getTransactionId());
            final MellatGatewaySettler settler = new MellatGatewaySettler(mellatWSWrapper, configReader, mellatResponseParams);
            final MellatWSResponse settle = settler.settle();
            logger.debug("Settle result is: {}", settle == null ? null : settle.toString());
        } catch (CommunicationException ex) {
            logger.warn("Error in settle : {}", ex.getMessage());
            exceptionLogger.warn("Error in settle ", ex);
            saveUnknownTransaction(transaction);

        }
        return true;
    }

    private void saveUnknownTransaction(TransactionInfo transaction) {
        UnknownTransaction unknownTransaction = new UnknownTransaction(transaction.getTransactionId(), transaction.getAccountNumber());
        unknowTransactionRepository.store(unknownTransaction);
    }


    @Override
    public PaymentResponseCode interpretResponse(TransactionInfo transactionInfo, Map<String, String> parameters) {
        MellatResponseParams mellatResponseParams = new MellatResponseParams(parameters);
        final boolean isSame = checkTransactionIsTheSame(transactionInfo, mellatResponseParams.getSaleOrderId());
        if (!isSame) {
            return PaymentResponseCode.CANCELED_BY_USER;
        }
        boolean result = new MellatWSResponse(parameters.get("ResCode")).isOK();
        if (result) {
            return PaymentResponseCode.PAYMENT_OK;
        }
        return PaymentResponseCode.CANCELED_BY_USER;
    }

    @Override
    public boolean isCommited(TransactionInfo transactionInfo, BankTransactionInfo bankTransactionInfo) {
        MellatResponseParams mellatResponseParams = null;
        try {
            mellatResponseParams = new MellatResponseParams(bankTransactionInfo.getPaymentResponseParams());
        } catch (NoMellatResponseException e) {
            return false;
        }

        final boolean isSame = checkTransactionIsTheSame(transactionInfo, mellatResponseParams.getSaleOrderId());
        if (!isSame) {
            return false;
        }
        final BankAccount bankAccount = bankAccountService.findAccount(transactionInfo.getAccountNumber());
        final PaymentGatewayConfiguration paymentGatewayConfiguration = bankAccount.getPaymentGatewayConfiguration();
        MellatGatewayConfigurationReader configReader = new MellatGatewayConfigurationReader(paymentGatewayConfiguration);

        logger.debug("About to polling transaction {},", transactionInfo.getTransactionId());

        final MellatWSResponse response = mellatWSWrapper.check(configReader.getAuthenticationData(), mellatResponseParams.getSaleOrderId(), mellatResponseParams.getSaleOrderId(), mellatResponseParams.getSaleReferenceId());

        logger.debug("Polling result {}", response == null ? null : response.toString());

        return response.isOK();
    }

    @Override
    public String getReferenceCodeFromResponse(Map<String, String> parameters) {
        if (parameters.containsKey(MellatResponseParams.SALE_REFERENCE_ID)) {
            parameters.get(MellatResponseParams.SALE_REFERENCE_ID);
        }
        return "";
    }

    private boolean checkTransactionIsTheSame(TransactionInfo transactionInfo, Long orderId) {
        return transactionInfo.getTransactionNumber() == orderId;
    }


}
