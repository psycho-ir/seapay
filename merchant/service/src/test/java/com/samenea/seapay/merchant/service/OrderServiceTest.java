package com.samenea.seapay.merchant.service;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.samenea.seapay.merchant.model.Merchant;
import com.samenea.seapay.merchant.model.Service;
import com.samenea.seapay.merchant.model.Order;
import com.samenea.seapay.merchant.repository.OrderRepository;
import static org.mockito.Mockito.*;

public class OrderServiceTest extends MerchantBaseServiceTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderRepository orderRepository;
	private final String transactionId = "TRN-100";
	private final String orderId = "ORD-100";
	private Order order = mock(Order.class);
	private Service service = mock(Service.class);
	private Merchant merchant = mock(Merchant.class);

	@Before
	public void before() {
		reset(orderRepository);
	}

	@Test
	public void findServiceOfTransaction_should_call_orderRepository_findServiceOfTransaction_method() {
		when(order.getOrderId()).thenReturn(orderId);
		when(order.getMerchant()).thenReturn(merchant);
		when(merchant.findService(anyString())).thenReturn(service);
		when(orderRepository.getByTransactionId(transactionId)).thenReturn(order);
		
		orderService.findServiceOfTransaction(transactionId);

		verify(orderRepository).getByTransactionId(transactionId);

	}

	// TODO: should change to a specific exception
	@Test(expected = IllegalArgumentException.class)
	public void should_throw_exception_when_order_does_not_exist() {
		when(orderRepository.getByTransactionId(transactionId)).thenReturn(null);
		orderService.findServiceOfTransaction(transactionId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void should_throw_exception_when_service_of_order_is_null() {
		when(order.getOrderId()).thenReturn(orderId);
		when(orderRepository.getByTransactionId(transactionId)).thenReturn(order);
		when(order.getMerchant()).thenReturn(merchant);
		when(merchant.findService(anyString())).thenReturn(null);
		
		orderService.findServiceOfTransaction(transactionId);
	}

    @Test
    public void findOrderByTransactionId_should_calls_orderRepositry(){
        orderService.findOrderByTransactionId(transactionId);

        verify(orderRepository).getByTransactionId(transactionId);
    }

}
