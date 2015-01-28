package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.model.PaymentGatewayConfiguration;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 11:28 AM
 */

public class SaderatGatewayConfigurationReaderTest {
    private Logger logger = LoggerFactory.getLogger(SaderatGatewayConfigurationReaderTest.class);
    private PaymentGatewayConfiguration configuration;
    private String config = "x_login!soroosh;x_transactionkey!123;";
    private String wrongConfig = "x_login!soroosh;x_transactionke!123;";

    @Before
    public void before() {
        configuration = new PaymentGatewayConfiguration(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_parameters_is_not_suitable_for_saderat() {
        configuration = new PaymentGatewayConfiguration(wrongConfig);
        new SaderatGatewayConfigurationReader(configuration);

    }

    @Test
    public void getLoginID_should_return_true_value() {
        final SaderatGatewayConfigurationReader saderatGatewayConfigurationReader = new SaderatGatewayConfigurationReader(configuration);

        Assert.assertEquals("soroosh", saderatGatewayConfigurationReader.getLoginID());

    }

    @Test
    public void getTransactionKey_should_return_true_value() {
        final SaderatGatewayConfigurationReader saderatGatewayConfigurationReader = new SaderatGatewayConfigurationReader(configuration);

        Assert.assertEquals("123", saderatGatewayConfigurationReader.getTransactionKey());
    }
}
