/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 12:14 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.rmi.RemoteException;

import static org.mockito.Mockito.*;


public class MellatGatewaySettlerTest {

    @Mock
    MellatWSWrapper mellatWSWrapper;
    @Mock
    MellatResponseParams parameterReader;
    @Mock
    MellatGatewayConfigurationReader configurationReader;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = IllegalArgumentException.class)
    public void constructor_show_throw_exception_when_mellatWSWrapper_is_null() {
        new MellatGatewaySettler(null, configurationReader, parameterReader);
    }

    @Test
    public void settle_should_call_mellatWSWrapper_settle_method() throws RemoteException {
        MellatGatewaySettler settler = new MellatGatewaySettler(mellatWSWrapper, configurationReader, parameterReader);
        settler.settle();

        verify(mellatWSWrapper).settle(any(AuthenticationData.class), anyLong(), anyLong(), anyLong());

    }


    @Test
    public void settle_should_retry_if_settle_throw_exception() throws RemoteException {
        reset(mellatWSWrapper);
        when(mellatWSWrapper.settle(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        MellatGatewaySettler settler = new MellatGatewaySettler(mellatWSWrapper, configurationReader, parameterReader);
        settler.settle();

        verify(mellatWSWrapper, times(3)).settle(any(AuthenticationData.class), anyLong(), anyLong(), anyLong());
    }
}
