package com.samenea.seapay.web.controller;

import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.commons.webmvc.model.MessageType;
import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.bank.model.BankTransactionService;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.merchant.IService;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.ModelMap;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 10:32 AM
 */
public class StartTransactionControllerTest {

    @InjectMocks
    @Spy
    private StartTransactionController startTransactionController;

    @Mock
    private SeaPayTrackingTranslator seaPayTrackingTranslator;
    @Mock
    private TrackingService trackingService;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private BankTransactionService bankService;
    @Mock
    private IOrderService orderService;
    @Mock
    private IService service;
    @Mock
    private IBankAccount bankAccount;
    private String bankName = "mellat";
    private String transactionId = "TRN-100";
    private String accNO= "ACC-100";
    private RedirectData paymentResponse;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        paymentResponse = RedirectData.createForPost("http://google.com",new HashMap<String, String>());

        when(bankAccount.getAccountNumber()) .thenReturn(accNO);
        when(bankAccount.getBankName()).thenReturn(bankName);
        when(service.getAccount(bankName)).thenReturn(bankAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void start_should_not_accept_null_transactionId() {
        startTransactionController.start(mock(ModelMap.class), bankName,null);
        verify(startTransactionController).addMessage(anyString(),any(MessageType.class));
    }
    @Test(expected = IllegalArgumentException.class)
    public void start_should_not_accept_empty_transactionId() {
        startTransactionController.start(mock(ModelMap.class), bankName," ");
        verify(startTransactionController).addMessage(anyString(),any(MessageType.class));
    }
    @Test(expected = IllegalArgumentException.class)
    public void start_should_not_accept_null_bankName() {
        startTransactionController.start(mock(ModelMap.class), null,transactionId);
        verify(startTransactionController).addMessage(anyString(),any(MessageType.class));
    }
    @Test(expected = IllegalArgumentException.class)
    public void start_should_not_accept_empty_bankName() {
        startTransactionController.start(mock(ModelMap.class), " ",transactionId);
        verify(startTransactionController).addMessage(anyString(),any(MessageType.class));
    }

    @Test
    public void start_should_call_findServiceOfTransaction(){
        when(orderService.findServiceOfTransaction(transactionId)).thenReturn(service);
        when(bankService.startTransaction(transactionId,bankAccount)).thenReturn(paymentResponse);
        ModelMap modelMap = mock(ModelMap.class);
        startTransactionController.start(modelMap, bankName,transactionId);

        verify(orderService).findServiceOfTransaction(transactionId);
    }


}
