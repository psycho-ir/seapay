package com.samenea.seapay.web.controller;

import com.google.common.collect.ImmutableMap;
import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.seapay.bank.model.BankTransactionService;
import com.samenea.seapay.bank.model.PaymentResponseCode;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * User: Soroosh Sarabadani
 * Date: 12/11/12
 * Time: 12:11 PM
 */
public class BankResponseControllerTest {

    @InjectMocks
    private BankResponseController bankResponseController;
    @Mock
    private BankTransactionService bankService;
    @Mock
    private IOrderService orderService;

    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private ModelMap modelMap;

    @Mock
    private ITransaction transaction;
    @Mock
    private ITransactionService transactionService;

    @Mock
    private TrackingService trackingService;
    @Mock
    private SeaPayTrackingTranslator seaPayTrackingTranslator;

    private final String bankName = "mellat";
    private final String ID_KEY = "id";
    private final String ID_VALUE = "123";
    private final String transactionId = "TRN-100";


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockCommon();
    }


    @Test
    public void interpret_should_find_all_request_parameters() {
        Map<String, String> parameters = ImmutableMap.of(ID_KEY, ID_VALUE);
        when(bankService.interpretPaymentResponse(transaction, parameters)).thenReturn(PaymentResponseCode.PAYMENT_OK);
        bankResponseController.interpret(modelMap, servletRequest, transactionId);

        verify(bankService).interpretPaymentResponse(transaction, parameters);
    }

    @Test
    public void interpret_should_set_modelMap_attributes_for_using_in_view() {
        Map<String, String> parameters = ImmutableMap.of(ID_KEY, ID_VALUE);
        when(bankService.interpretPaymentResponse(transaction, parameters)).thenReturn(PaymentResponseCode.PAYMENT_OK);
        bankResponseController.interpret(modelMap, servletRequest, transactionId);
        verify(modelMap).addAttribute(eq("result"), anyString());
        verify(modelMap).addAttribute(eq("order"), any(IOrder.class));
    }

    private void mockCommon() {
        when(servletRequest.getParameterMap()).thenReturn(createParametersMap());
        when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        when(trackingService.record(anyString(),anyString())).thenReturn(null);

        when(orderService.findOrderByTransactionId(transactionId)).thenReturn(new IOrder() {
            @Override
            public String getTransactionId() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getCallbackUrl() {
                return "http://clientCallbackUrl";
            }

            @Override
            public String getCustomerId() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean checkIsFor(String transactionId, String merchantId, String serviceName) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }


    private Map<String, String[]> createParametersMap() {
        Map<String, String[]> parameters = ImmutableMap.of(ID_KEY, new String[]{ID_VALUE});
        return parameters;
    }
}
