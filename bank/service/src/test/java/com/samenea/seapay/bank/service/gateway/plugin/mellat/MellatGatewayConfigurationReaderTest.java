/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 5:37 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class MellatGatewayConfigurationReaderTest {
    private String config = "username!soroosh;password!123;terminalId!111;";
    private PaymentGatewayConfiguration gatewayConfiguration;


    @Before
    public void before() {
        gatewayConfiguration = new PaymentGatewayConfiguration(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_constructor_throw_exception_null_gatewayConfiguration() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(null);
    }

    @Test(expected = SamenRuntimeException.class)
    public void should_constructor_throw_exception_when_config_is_not_compatible_for_mellat() {
        final String wrongConfig = "username!soroosh;";
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(new PaymentGatewayConfiguration(wrongConfig));

    }

    @Test
    public void should_constructor_create_object_when_gatewayConfiguration_is_not_null() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(gatewayConfiguration);
    }

    @Test
    public void getUsername_should_return_true_value() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(gatewayConfiguration);
        final String username = mellatGatewayConfigurationReader.getUsername();

        Assert.assertEquals("soroosh", username);

    }

    @Test
    public void getPassword_should_return_true_value() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(gatewayConfiguration);
        final String password = mellatGatewayConfigurationReader.getPassword();

        Assert.assertEquals("123", password);
    }

    @Test
    public void getTerminalId_should_return_true_value() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(gatewayConfiguration);
        final Long terminalId = mellatGatewayConfigurationReader.getTerminalId();

        Assert.assertEquals(new Long(111), terminalId);
    }

    @Test
    public void getAuthenticationData_should_return_suitable_value() {
        MellatGatewayConfigurationReader mellatGatewayConfigurationReader = new MellatGatewayConfigurationReader(gatewayConfiguration);
        final AuthenticationData authenticationData = mellatGatewayConfigurationReader.getAuthenticationData();

        Assert.assertEquals(new Long(111).longValue(), authenticationData.getTerminalId());
        Assert.assertEquals("soroosh", authenticationData.getUsername());
        Assert.assertEquals("123", authenticationData.getPassword());
    }

}
