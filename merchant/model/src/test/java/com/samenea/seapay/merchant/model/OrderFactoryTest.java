package com.samenea.seapay.merchant.model;

import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.merchant.repository.MerchantRepository;
import com.samenea.seapay.merchant.repository.OrderRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class OrderFactoryTest {
	private final String merchantId = "M-100";
    private final String passowrd="PASS";
	private Order order;
	private String orderId = "ORDER-100";
	private String serviceName = "S-100";
	private Merchant merchant = new Merchant(merchantId,passowrd);
	private String callbackUrl = "http://abc.com";
	private String transactionId = "TRN-100";
	private String customerId = "CUSTOMER-100";

	@InjectMocks
	private OrderFactory orderFactory = new OrderFactory();

    @Mock
    private IDGenerator idGenerator;

	@Mock
	private IDGeneratorFactory idGeneratorFactory;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private MerchantRepository merchantRepository;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		mockOrderRepository();
		mockMerchantRepository();
	}

	@Test
	public void should_create_order_and_set_its_parameters() {
        when(idGenerator.getNextID()).thenReturn(orderId);
        when(idGeneratorFactory.getIDGenerator(Order.TOKEN_ID)).thenReturn(idGenerator);
		Order order = orderFactory.createOrder(serviceName, merchantId, callbackUrl, transactionId, customerId);
		Assert.assertEquals(serviceName, order.getServiceName());
		Assert.assertEquals(merchant, order.getMerchant());
		Assert.assertEquals(callbackUrl, order.getCallbackUrl());
		Assert.assertEquals(transactionId, order.getTransactionId());
		Assert.assertEquals(customerId, order.getCustomerId());
	}

	@Test
	public void should_call_idGenerator_getNextId_in_creation() {
        when(idGenerator.getNextID()).thenReturn(orderId);
        when(idGeneratorFactory.getIDGenerator(Order.TOKEN_ID)).thenReturn(idGenerator);
		Order order = orderFactory.createOrder(serviceName, merchantId, callbackUrl, transactionId, customerId);
		verify(idGenerator).getNextID();
	}

	@Test
	public void should_call_repostiry_store_in_creation() {
        when(idGenerator.getNextID()).thenReturn(orderId);
        when(idGeneratorFactory.getIDGenerator(Order.TOKEN_ID)).thenReturn(idGenerator);
		Order order = orderFactory.createOrder(serviceName, merchantId, callbackUrl, transactionId, customerId);
		verify(orderRepository).store(any(Order.class));
	}

	private void mockOrderRepository() {
		when(orderRepository.store(any(Order.class))).thenAnswer(new Answer<Order>() {

			@Override
			public Order answer(InvocationOnMock invocation) throws Throwable {
				return (Order) invocation.getArguments()[0];
			}
		});
	}

	private void mockMerchantRepository() {
		when(merchantRepository.getByMerchantId(merchantId)).thenAnswer(new Answer<Merchant>() {
			@Override
			public Merchant answer(InvocationOnMock invocation) throws Throwable {
				return merchant;
			}
		});

	}
}
