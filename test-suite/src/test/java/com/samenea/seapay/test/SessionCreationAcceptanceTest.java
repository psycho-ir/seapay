package com.samenea.seapay.test;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionService;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.transaction.ISeapayGatewayService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.annotation.DataSets;

import java.util.Calendar;

import static org.mockito.Mockito.when;

/**
 * Acceptance tests about Session creations are here.
 *
 * @author: Soroosh Sarabadani
 * Date: 12/31/12
 * Time: 9:57 AM
 */

@DataSets(setUpDataSet = "/sample-data/SessionCreationAcceptanceTest.xml")
public class SessionCreationAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private ISeapayGatewayService seapayGatewayService;

    @Autowired
    private Environment environment;

    @Autowired
    ISessionService sessionService;
    private final String password = "123456";
    private final String serviceId = "S-100";
    private final String merchantId = "M-100";

    @Before
    public void before() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2011, 5, 6, 23, 24, 45);
        when(environment.getCurrentDate()).thenReturn(calendar.getTime());
    }

    @Test
    @Rollback(false)
    public void should_create_session_successfully_and_persist_it() {

        final String sessionId = seapayGatewayService.createSession(merchantId, serviceId, password);
        final ISession session = sessionService.getSession(sessionId);
        Assert.assertEquals(merchantId, session.getMerchantId());
        Assert.assertEquals(environment.getCurrentDate(), session.getCreateDate());


    }

    @Test(expected = RequestNotValidException.class)
    public void should_throw_exception_if_merchant_password_is_not_true(){
        final String sessionId = seapayGatewayService.createSession(merchantId, serviceId, "pass");
    }



    @Test(expected = NotFoundException.class)
    public void should_throw_exception_if_merchantId_is_not_valid() {
        final String notValidMerchantId = "M-101";

        final String sessionId = seapayGatewayService.createSession(notValidMerchantId, serviceId, password);

    }


}
