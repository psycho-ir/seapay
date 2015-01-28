package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.transaction.TransactionInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 12:53 PM
 */

public class RedirectDataBuilderTest {
    private final String description = "description";
    private final int amount = 1000;
    private final String loginId = "1234";
    private final String transactionKey = "9876";
    private Logger logger = LoggerFactory.getLogger(RedirectDataBuilderTest.class);
    @Mock
    private TransactionInfo transactionInfo;
    @Mock
    private SaderatGatewayConfigurationReader configurationReader;
    @Mock
    private Environment environment;
    private String callbackUrl = "https://dsss";

    @Spy
    private RedirectDataBuilder redirectDataBuilder = mock(RedirectDataBuilder.class);
    private Date date = Calendar.getInstance().getTime();


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(environment.getCurrentDate()).thenReturn(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_configurationReader_is_null() {
        new RedirectDataBuilder(null, transactionInfo, callbackUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_transactionInfo_is_null() {
        new RedirectDataBuilder(configurationReader, null, callbackUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_callbackUrl_is_null() {
        new RedirectDataBuilder(configurationReader, transactionInfo, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_should_throw_exception_when_callbackUrl_is_empty() {
        new RedirectDataBuilder(configurationReader, transactionInfo, "");
    }



}
