package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;

/**
 * Objects of this class can read PaymentGatewayConfigurations that are for Saderat gateway.
 *
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 11:20 AM
 */

public class SaderatGatewayConfigurationReader {
    private final String loginID;
    private final String transactionKey;

    public SaderatGatewayConfigurationReader(PaymentGatewayConfiguration paymentGatewayConfiguration) {

        this.loginID = paymentGatewayConfiguration.get(SaderatParams.X_LOGIN);
        this.transactionKey = paymentGatewayConfiguration.get(SaderatParams.X_TRANSACTIONKEY);
        if (this.loginID == null || this.transactionKey == null) {
            throw new IllegalArgumentException("paymentGatewayConfiguration is not compatible with mellat.");
        }

    }

    public String getLoginID() {
        return loginID;
    }

    public String getTransactionKey() {
        return transactionKey;
    }
}
