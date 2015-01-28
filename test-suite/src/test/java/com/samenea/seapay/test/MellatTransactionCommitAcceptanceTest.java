package com.samenea.seapay.test;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.transaction.ISeapayGatewayService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.annotation.DataSets;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/6/13
 * Time: 1:30 PM
 */

public class MellatTransactionCommitAcceptanceTest extends BaseAcceptanceTest {
    @Autowired
    private ISeapayGatewayService seapayGatewayService;

    @Autowired
    private Environment environment;

    @Autowired
    private MellatWSWrapper mellatWSWrapper;

    private final static Date date = Calendar.getInstance().getTime();
    private final int amount = 10000;
    private final String transactionId = "TRN-100";
    private final Long orderId = 100L;
    private final Long referenceId = 123L;
    private final String sessionId = "7323df46-cf87-4560-be3b-39ec3d2b9047";


    @Before
    public void before() {
        reset(mellatWSWrapper);
        when(environment.getCurrentDate()).thenReturn(date);
    }

    @Test
    @Rollback(false)
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionCommitAcceptanceTest.xml")
    public void should_commit_transaction() {
        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenReturn(new MellatWSResponse("0"));
        when(mellatWSWrapper.settle(any(AuthenticationData.class), anyLong(), anyLong(), anyLong())).thenReturn(new MellatWSResponse("0"));
        seapayGatewayService.commitTransaction(sessionId, transactionId, amount);
    }

    @Test
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionCommitAcceptanceTest.xml")
    public void should_retry_when_verify_throws_exception() {

        when(mellatWSWrapper.commitTransaction(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(referenceId))).thenThrow(new CommunicationException()).thenReturn(new MellatWSResponse("0"));
        when(mellatWSWrapper.check(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(referenceId))).thenThrow(new CommunicationException()).thenReturn(new MellatWSResponse("0"));
        when(mellatWSWrapper.settle(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(referenceId))).thenReturn(new MellatWSResponse("0"));

        seapayGatewayService.commitTransaction(sessionId, transactionId, amount);

        verify(mellatWSWrapper, times(2)).check(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(referenceId));
        verify(mellatWSWrapper).settle(any(AuthenticationData.class), eq(orderId), eq(orderId), eq(referenceId));
    }

    @Test(expected = RequestNotValidException.class)
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionCommitAcceptanceTest.xml")
    public void should_throw_exception_when_amount_is_changed() {
        seapayGatewayService.commitTransaction(sessionId, transactionId, 999);
    }

}
