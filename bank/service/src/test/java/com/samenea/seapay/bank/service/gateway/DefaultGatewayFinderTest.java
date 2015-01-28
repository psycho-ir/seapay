/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 3:56 PM
 */
package com.samenea.seapay.bank.service.gateway;

import com.samenea.seapay.bank.gateway.model.GatewayFinder;
import com.samenea.seapay.bank.gateway.model.GatewayNotFoundException;
import com.samenea.seapay.bank.gateway.model.GatewayPlugin;
import com.samenea.seapay.bank.service.BankBaseIntegrationServiceTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultGatewayFinderTest extends BankBaseIntegrationServiceTest {
    @Autowired
    private GatewayFinder gatewayFinder;

    @Test
    public void findByName_should_return_gateway_of_mellat() {
        GatewayPlugin plugin = gatewayFinder.findByName("mellat");
        Assert.assertNotNull(plugin);
    }

    @Test(expected = GatewayNotFoundException.class)
    public void findByName_should_throw_exception_when_plugin_does_not_exist(){
        gatewayFinder.findByName("NNN");
    }

}
