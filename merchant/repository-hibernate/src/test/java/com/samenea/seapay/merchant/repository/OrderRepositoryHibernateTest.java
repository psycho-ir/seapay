package com.samenea.seapay.merchant.repository;

import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.model.Order;
import com.samenea.seapay.merchant.model.OrderFactory;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import test.annotation.DataSets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderRepositoryHibernateTest extends MerchantBaseRepositoryTest {
    private IDGenerator idGenerator;
    @Autowired
    private IDGeneratorFactory idGeneratorFactory;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private OrderRepository orderRepository;
    private final String serviceName = "S-100";
    private final String merchantId = "M-100";
    private final String transactionId = "TRN-100";
    private final String customerId = "C-100";
    private final String callbackUrl = "http://www.yahooo.com";
    private final String orderId = "O-100";

    @Test
    @Rollback(false)
    @DataSets(setUpDataSet = "/sample-data/merchant-with-service-exists-sample-data.xml")
    public void should_save_order_with_saved_service_and_merchant() {
        idGenerator = mock(IDGenerator.class);
        when(idGenerator.getNextID()).thenReturn(orderId);
        when(idGeneratorFactory.getIDGenerator(Order.TOKEN_ID)).thenReturn(idGenerator);
        OrderFactory orderFactory = new OrderFactory();
        orderFactory.createOrder(serviceName, merchantId, callbackUrl, transactionId, customerId);
    }

    @Test
    @DataSets(setUpDataSet = "/sample-data/merchant-with-service-and-order-exists-sample-data.xml")
    public void should_find_order_with_specified_transactionId() {
        IOrder order = orderRepository.getByTransactionId("TRN-100");

        Assert.assertNotNull(order);
    }
}
