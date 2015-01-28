package com.samenea.seapay.web.controller;


import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.tracking.service.TrackingService;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.web.model.TransactionViewModel;
import com.samenea.seapay.web.translation.SeaPayTrackingTranslator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: Soroosh Sarabadani
 * Date: 12/15/12
 * Time: 1:17 PM
 */
public class BankSelectionControllerTest {

    @InjectMocks
    private BankSelectionController bankSelectionController;
    @Mock
    private IMerchantService merchantService;
    @Mock
    private  ITransaction iTransaction;
    private final String transactionId = "TRN-100";
    @Mock
    private ModelMap modelMap;
    @Mock
    private IOrderService orderService;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private IOrder order;
    @Mock
    private TrackingService trackingService;
    @Mock
    private SeaPayTrackingTranslator seaPayTrackingTranslator;

    private String callbackUrl="www.samen.ir";
    private TransactionViewModel transactionViewModel;
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(iTransaction.getAmount()).thenReturn(1000);
        when(iTransaction.getDescription()).thenReturn("this is test");
        when(order.getCallbackUrl()).thenReturn(callbackUrl);
        when(transactionService.getTransaction(transactionId)).thenReturn(iTransaction);
        transactionViewModel=new TransactionViewModel(iTransaction.getAmount(),transactionId,iTransaction.getDescription());

    }

    @Test
    public void findPermittedBanks_should_get_banks_from_merchant_service() {
        when(orderService.findOrderByTransactionId(transactionId)).thenReturn(order);
        when(transactionService.getTransaction(transactionId)).thenReturn(iTransaction);
        bankSelectionController.findPermittedBanks(transactionId, modelMap);
        verify(merchantService).findBanks(transactionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findPermittedBanks_should_not_accept_null_transactionId() {
        bankSelectionController.findPermittedBanks(null, modelMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findPermittedBanks_should_not_accept_empty_transactionId() {
        bankSelectionController.findPermittedBanks(" ", modelMap);
    }

    @Test
    public void findPermittedBanks_show_set_order_in_modelMap() {

        when(orderService.findOrderByTransactionId(transactionId)).thenReturn(order);
        bankSelectionController.findPermittedBanks(transactionId, modelMap);
        verify(modelMap).addAttribute("order", order);
    }


    @Test(expected = IllegalArgumentException.class)
    public void cancelTransaction_should_not_accept_empty_transactionId() {
       bankSelectionController.cancelTransaction("");
    }

    @Test(expected = NotFoundException.class)
    public void cancelTransaction_should_throw_NotFoundException_when_transactionId_notfound(){

        doThrow(NotFoundException.class).when(transactionService).getTransaction(transactionId);
        bankSelectionController.cancelTransaction(transactionId);

    }
}
