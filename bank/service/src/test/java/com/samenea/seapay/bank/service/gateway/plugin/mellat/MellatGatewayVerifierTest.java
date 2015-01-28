/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 3:23 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.rmi.RemoteException;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class MellatGatewayVerifierTest {
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
        new MellatGatewayVerifier(null, configurationReader, parameterReader);
    }

    @Test
    public void verify_should_call_mellatWSWrapper_verify_method() throws RemoteException {
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        when(mellatWSWrapper.check(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        MellatGatewayVerifier verifier = new MellatGatewayVerifier(mellatWSWrapper, configurationReader, parameterReader);
        verifier.verify();

        verify(mellatWSWrapper).commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong());

    }

    @Test
    public void verify_should_retry_on_inquiry_if_verify_throw_exception() throws RemoteException {
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        when(mellatWSWrapper.check(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenThrow(Exception.class, Exception.class).thenReturn(new MellatWSResponse("0"));
        MellatGatewayVerifier verifier = new MellatGatewayVerifier(mellatWSWrapper, configurationReader, parameterReader);
        verifier.verify();

        verify(mellatWSWrapper, times(3)).check(any(AuthenticationData.class), anyLong(), anyLong(), anyLong());
    }

    @Test(expected = VerifyException.class)
    public void verify_should_throw_exception_when_ws_response_is_not_ok() {
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenReturn(new MellatWSResponse("1"));

        MellatGatewayVerifier verifier = new MellatGatewayVerifier(mellatWSWrapper, configurationReader, parameterReader);
        verifier.verify();

    }

}
