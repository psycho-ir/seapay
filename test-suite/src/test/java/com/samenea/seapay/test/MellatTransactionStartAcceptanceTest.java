package com.samenea.seapay.test;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.AuthenticationData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import com.samenea.seapay.merchant.IBankAccount;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.annotation.DataSets;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/5/13
 * Time: 3:03 PM
 */

@Ignore
public class MellatTransactionStartAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private IBankTransactionService bankTransactionService;

    @Autowired
    private Environment environment;

    @Autowired
    private MellatWSWrapper mellatWSWrapper;

    private static Date date = Calendar.getInstance().getTime();

    private final String transactionId = "TRN-100";
    private IBankAccount bankAccount;
    private long orderId = 100;
    private long amount = 10000;
    private String callbackUrl = "http://google.com";
    private String callback = "https://pgw.bpm.bankmellat.ir/pgwchannel/startpay.mellat";

    @Before
    public void before() {
        mockEnvironment();
        bankAccount = new IBankAccount() {
            @Override
            public String getAccountNumber() {
                return "1223";
            }

            @Override
            public String getTitle() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getBankName() {
                return "mellat";
            }
        };
    }


    @Test
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionStartAcceptanceTest.xml")
    public void should_start_transaction_successfully() {
        when(mellatWSWrapper.payRequest(any(AuthenticationData.class), eq(orderId), eq(amount), anyString(), anyString())).thenReturn(new MellatWSResponse("0,11110"));
        IRedirectData redirectData = bankTransactionService.startTransaction(transactionId, bankAccount);
        Assert.assertEquals(IRedirectData.HttpMethod.POST, redirectData.getHttpMethod());
        Assert.assertEquals(callback, redirectData.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionStartAcceptanceTest.xml")
    public void should_throw_exception_when_mellat_response_is_not_ok(){
        when(mellatWSWrapper.payRequest(any(AuthenticationData.class), eq(orderId), eq(amount), anyString(), anyString())).thenReturn(new MellatWSResponse("1"));
        IRedirectData redirectData = bankTransactionService.startTransaction(transactionId, bankAccount);
    }

/*    @Test
    @DataSets(setUpDataSet = "/sample-data/MellatTransactionStartAcceptanceTest.xml")
    public void should_throw_exception_when_bankAccount_is_not_for_bank() {
//        when(mellatWSWrapper.payRequest(any(AuthenticationData.class), eq(orderId), eq(amount), anyString(), anyString())).thenReturn(new MellatWSResponse("0"));
        IBankAccount anotherBankAccount = new IBankAccount() {
            @Override
            public String getAccountNumber() {
                return "1224";
            }

            @Override
            public String getTitle() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getBankName() {
                return "mellat";
            }
        };

        IRedirectData redirectData = bankTransactionService.startTransaction(transactionId, anotherBankAccount);

    }*/

    private void mockEnvironment() {
        when(environment.getCurrentDate()).thenReturn(date);
    }

}
