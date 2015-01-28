package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.model.*;
import com.samenea.seapay.bank.service.gateway.plugin.AbstractGatewayPlugin;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 11:06 AM
 */
@Service
public class SaderatGatewayPlugin extends AbstractGatewayPlugin {
    public static final String OK = "1";
    private Logger logger = LoggerFactory.getLogger(SaderatGatewayPlugin.class);

    private Integer unknownToFailedThresholdMin = 15;

    @Autowired
    private SaderatGatewayConnectionUtil saderatGatewayConnectionUtil;

    @Autowired
    private BankTransactionInfoRepository bankTransactionInfoRepository;

    private String saderatVerificationUrl = "https://damoon.bsi.ir/DamoonVerificationController";

    @Override
    public IRedirectData request(TransactionInfo transactionInfo) {

        SaderatGatewayConfigurationReader configReader = new SaderatGatewayConfigurationReader(getPaymentGatewayConfiguration(transactionInfo));

        final String requestCallback = getCallback(transactionInfo);

        try {
            ((ITransaction) transactionInfo).markUnknown();
            return new RedirectDataBuilder(configReader, transactionInfo, requestCallback).createPaymentResponse();
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Saderat Key has problem", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash Algorithm is not ok.", e);
        }
    }

    @Override
    public boolean verify(TransactionInfo transaction, Map<String, String> responseParameters) {
        try {
            final PaymentResponseCode responseCode = checkVerification(transaction);
            if (responseCode == PaymentResponseCode.PAYMENT_OK) {
                return true;
            }
            return false;
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Saderat Key has problem", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash Algorithm is not ok.", e);
        }
    }

    @Override
    public PaymentResponseCode interpretResponse(TransactionInfo transactionInfo, Map<String, String> parameters) {
        try {
            if (!verifyResponse(transactionInfo, parameters)) {
                return PaymentResponseCode.CANCELED_BY_USER;
            }
            return checkResponseCode(parameters);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Saderat Key has problem", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash Algorithm is not ok.", e);
        }
    }


    @Override
    public boolean isCommited(TransactionInfo transactionInfo, BankTransactionInfo bankTransactionInfo) {
        try {
            final Map<String, String> parameters = bankTransactionInfo.getTransactionStartParams();
            if (parameters == null || parameters.size() == 0) {
                return false;
            }
            String commitedResult = saderatGatewayConnectionUtil.request(saderatVerificationUrl, parameters);
            SaderatTransactionTrackingResponse response = new SaderatTransactionTrackingResponse(commitedResult);
            bankTransactionInfo.savePaymentResponseParams(response.getParameters());
            if (response.getParameters().keySet().contains(SaderatParams.X_TRANSACTION_ID)) {
                bankTransactionInfo.setReferenceCode(response.getParameters().get(SaderatParams.X_TRANSACTION_ID));
            }
            bankTransactionInfoRepository.store(bankTransactionInfo);
            if (response.isCommited()) {
                return true;
            }
            if (response.isPending()) {
                if (isPendingDurationMoreThanExpected(transactionInfo)) {
                    return false;
                }

                throw new SamenRuntimeException(String.format("Saderat Transaction %s is pending.", transactionInfo.getTransactionId()));
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash Algorithm is not ok.", e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException("Key management is not ok.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Url has problem.", e);
        }

    }

    @Override
    public String getReferenceCodeFromResponse(Map<String, String> parameters) {
        if (parameters.get(SaderatParams.X_TRANSACTION_ID) != null) {
            return parameters.get(SaderatParams.X_TRANSACTION_ID);
        }
        return "";
    }

    private boolean isPendingDurationMoreThanExpected(TransactionInfo transactionInfo) {
        return new Date(transactionInfo.getLastUpdateDate().getTime() + unknownToFailedThresholdMin * 60 * 1000).before(environment.getCurrentDate());
    }

    private PaymentResponseCode checkResponseCode(Map<String, String> parameters) {
        if (parameters.get("x_response_code").equals(OK)) {
            return PaymentResponseCode.PAYMENT_OK;
        }
        return PaymentResponseCode.CANCELED_BY_USER;
    }

    private PaymentResponseCode checkVerification(TransactionInfo transactionInfo) throws InvalidKeyException, NoSuchAlgorithmException {
        final BankTransactionInfo bankTransactionInfo = bankTransactionInfoRepository.findByTransactionId(transactionInfo.getTransactionId());
        final Map<String, String> paymentResponseParams = bankTransactionInfo.getPaymentResponseParams();
        final boolean verified = verifyResponse(transactionInfo, paymentResponseParams);
        if (!verified) {
            return PaymentResponseCode.CANCELED_BY_USER;
        }
        return checkResponseCode(paymentResponseParams);
    }

    private boolean verifyResponse(TransactionInfo transactionInfo, Map<String, String> parameters) throws InvalidKeyException, NoSuchAlgorithmException {
        if (parameters == null || parameters.get(SaderatParams.X_TRANSACTION_ID) == null) {
            throw new IllegalStateException(String.format("Response for transaction %s is not ready for verification", transactionInfo.getTransactionId()));
        }
        final BankAccount bankAccount = bankAccountService.findAccount(transactionInfo.getAccountNumber());
        final PaymentGatewayConfiguration paymentGatewayConfiguration = bankAccount.getPaymentGatewayConfiguration();
        final SaderatGatewayConfigurationReader configReader = new SaderatGatewayConfigurationReader(paymentGatewayConfiguration);
        List<String> codesForHashing = new ArrayList<String>();
        codesForHashing.add(parameters.get(SaderatParams.X_TRANSACTION_ID));
        codesForHashing.add(parameters.get(SaderatParams.X_RESPONSE_CODE));
        codesForHashing.add(parameters.get(SaderatParams.X_RESPONSE_SUB_CODE));
        codesForHashing.add(parameters.get(SaderatParams.X_RESPONSE_REASON_CODE));
        codesForHashing.add(parameters.get(SaderatParams.X_RESPONSE_REASON_TEXT));
        codesForHashing.add(parameters.get(SaderatParams.X_LOGIN));
        final String sequence = parameters.get(SaderatParams.X_FP_SEQUENCE);
        codesForHashing.add(sequence);
        codesForHashing.add(parameters.get(SaderatParams.X_FP_TIMESTAMP));
        codesForHashing.add(parameters.get(SaderatParams.X_AMOUNT));
        codesForHashing.add(parameters.get(SaderatParams.X_CURRENCY_CODE));
        String hashedString = new SaderatHashUtil().hash(configReader.getTransactionKey(), codesForHashing);

        return hashedString.equals(parameters.get(SaderatParams.X_FP_HASH)) && checkTransactionIsTheSame(transactionInfo, sequence);
    }

    private boolean checkTransactionIsTheSame(TransactionInfo transactionInfo, String sequence) {
        return transactionInfo.getTransactionNumber() == Long.valueOf(sequence);
    }
}
