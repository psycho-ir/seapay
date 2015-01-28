package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.seapay.bank.service.BankBaseIntegrationServiceTest;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Soroosh Sarabadani
 * Date: 9/23/13
 * Time: 10:18 AM
 */

public class MellatGatewayPluginIntegrationTest extends BankBaseIntegrationServiceTest {

    @Autowired
    private MellatWSWrapper wsWrapper;

    private final AuthenticationData authenticationData;

    public MellatGatewayPluginIntegrationTest() {
        authenticationData = new AuthenticationData(1314292,"ebsa","ebsa");
    }

    @Ignore
    @Test
    public void a(){
        Assert.assertNotNull(wsWrapper);
        final MellatWSResponse mellatWSResponse = wsWrapper.payRequest(authenticationData, 1l, 1000l, "http://google.com", "123");
        System.out.println(mellatWSResponse);

    }
}


