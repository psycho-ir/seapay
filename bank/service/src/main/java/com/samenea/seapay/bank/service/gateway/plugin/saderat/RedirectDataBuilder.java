package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 11:47 AM
 */

@Configurable
public class RedirectDataBuilder {
    private Logger logger = LoggerFactory.getLogger(RedirectDataBuilder.class);

    @Autowired
    private Environment environment;

    @Autowired
    private SaderatHashUtil saderatHashUtil;
    private final SaderatGatewayConfigurationReader saderatGatewayConfigurationReader;
    private final TransactionInfo transactionInfo;
    private final String callbackUrl;

    private RedirectData redirectData;
    private String saderatUrl = "https://Damoon.bsi.ir/DamoonPrePaymentController";

    public RedirectDataBuilder(SaderatGatewayConfigurationReader saderatGatewayConfigurationReader, TransactionInfo transactionInfo, String callbackUrl) {
        Assert.notNull(saderatGatewayConfigurationReader, "saderatGatewayConfigurationReader cannot be null.");
        Assert.notNull(transactionInfo, "transactionInfo cannot be null.");
        Assert.notNull(callbackUrl, "callbackUrl cannot be null.");
        Assert.hasText(callbackUrl, "callbackUrl cannot be empty.");
        this.saderatGatewayConfigurationReader = saderatGatewayConfigurationReader;
        this.transactionInfo = transactionInfo;
        this.callbackUrl = callbackUrl;
    }

    private Map<String, String> createParameters() throws NoSuchAlgorithmException, InvalidKeyException {
        final String currency = "Rial";
        Long timeStamp = environment.getCurrentDate().getTime() / 1000;
        final String amount = new Integer(transactionInfo.getAmount()).toString();
        final String sequence = new Long(transactionInfo.getTransactionNumber()).toString();
        final String loginID = saderatGatewayConfigurationReader.getLoginID();
        final String transactionKey = saderatGatewayConfigurationReader.getTransactionKey();
        final String timestamp = timeStamp.toString();

        Map<String, String> result = new HashMap<String, String>();
        result.put(SaderatParams.X_AMOUNT, amount);
        result.put(SaderatParams.X_DESCRIPTION, transactionInfo.getDescription());
        result.put(SaderatParams.X_FP_SEQUENCE, sequence);
        result.put(SaderatParams.X_FP_RECIEPTPAGE, this.callbackUrl);
        result.put(SaderatParams.X_FP_HASH, saderatHashUtil.createRequestHashKey(transactionKey, loginID, sequence, timestamp, amount, currency));
        result.put(SaderatParams.X_LOGIN, loginID);
        result.put(SaderatParams.X_FP_TIMESTAMP, timestamp);
        result.put(SaderatParams.X_CURRENCY_CODE, currency);

        return result;
    }

    public RedirectData createPaymentResponse() throws InvalidKeyException, NoSuchAlgorithmException {
        this.redirectData = RedirectData.createForPost(saderatUrl, createParameters());
        return this.redirectData;
    }


}
