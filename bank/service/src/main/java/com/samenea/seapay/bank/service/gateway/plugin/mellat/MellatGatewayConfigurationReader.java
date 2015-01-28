/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 5:36 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import org.springframework.util.Assert;

public class MellatGatewayConfigurationReader {
    public final static String TERMINAL_ID = "terminalId";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    private final String username;
    private final String password;
    private final Long terminalId;

    private final  AuthenticationData authenticationData;
    private PaymentGatewayConfiguration paymentGatewayConfiguration;




    public MellatGatewayConfigurationReader(PaymentGatewayConfiguration paymentGatewayConfiguration) {
        Assert.notNull(paymentGatewayConfiguration, "paymentGatewayConfiguration cannot be null.");
        this.paymentGatewayConfiguration = paymentGatewayConfiguration;
        this.username = this.paymentGatewayConfiguration.get(USERNAME);
        this.password = this.paymentGatewayConfiguration.get(PASSWORD);
        final String terminalIdString = this.paymentGatewayConfiguration.get(TERMINAL_ID);

        if (this.username == null || this.password == null || terminalIdString == null) {
            throw new SamenRuntimeException("paymentGatewayConfiguration is not compatible with mellat.");
        }

        this.terminalId = Long.parseLong(terminalIdString);
        this.authenticationData = new AuthenticationData(this.terminalId,this.username,this.password);

    }

    public AuthenticationData getAuthenticationData(){
        return this.authenticationData;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Long getTerminalId() {
        return this.terminalId;

    }

    @Override
    public String toString() {
        return "MellatGatewayConfigurationReader{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", terminalId=" + terminalId +
                '}';
    }
}
