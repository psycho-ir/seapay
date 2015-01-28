package com.samenea.seapay.client.web;

import com.samenea.commons.component.utils.UserContext;
import com.samenea.seapay.client.PaymentManager;
import com.samenea.seapay.client.impl.CommunicationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.security.Principal;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: Jalal Ashrafi
 * Date: 1/19/13
 */
@ContextConfiguration(locations = { "classpath:context.xml","classpath:contexts/mock.xml" })
public class TransactionStartControllerTest extends AbstractJUnit4SpringContextTests {
    public static final String SPRING_REDIRECT_PREFIX = "redirect:";
    @Autowired
    TransactionStartController transactionStartController;
//    @Autowired
//    OrderService orderService;

    @Autowired
    PaymentManager paymentManager;

    @Autowired()
    UserContext userContext;

    @Value("${seapay.seapayUrl}")
    String seapayUrl;

    private String orderId;
    private String transactionId;
    private String currentUserName;

    @Before
    public void setup(){
        orderId = "ORDER-1";
        transactionId = "TRN-1";
        currentUserName = "exampleUserName";
        reset(paymentManager);
        when(userContext.currentUser()).thenReturn(new Principal() {
            @Override
            public String getName() {
                return currentUserName;
            }
        });
    }

    @Test
    public void startTransaction_should_redirect_to_seapay_with_created_transactionId(){
//        when(orderService.orderAmount(orderId)).thenReturn(amount);
        when(paymentManager.startTransaction(orderId, currentUserName)).thenReturn(transactionId);
        String result = transactionStartController.startTransaction(orderId);
        assertEquals(SPRING_REDIRECT_PREFIX + seapayUrl + transactionId, result);
    }
    @Test
    public void startTransaction_should_save_error_and_return_to_input_if_there_is_communicationException(){
        when(paymentManager.startTransaction(orderId, currentUserName)).thenThrow(new CommunicationException("Simulated Communication exception"));
        String result = transactionStartController.startTransaction(orderId);
        assertEquals("/confirm/"+orderId, result);
        assertEquals(1,transactionStartController.getMessages().size());
    }
    @Test
    public void startTransaction_should_use_default_customer_name_if_no_user_is_logged_in(){
        when(userContext.currentUser()).thenReturn(null);
        transactionStartController.startTransaction(orderId);
        verify(paymentManager).startTransaction(orderId, TransactionStartController.DEFAULT_USER_NAME);
    }
}
