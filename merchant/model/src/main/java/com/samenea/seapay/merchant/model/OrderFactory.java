package com.samenea.seapay.merchant.model;

import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.merchant.repository.MerchantRepository;
import com.samenea.seapay.merchant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable(preConstruction = true, dependencyCheck = true)
public class OrderFactory {
    @Autowired
    private IDGeneratorFactory idGeneratorFactory;

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private MerchantRepository merchantRepository;

	public Order createOrder(String serviceName, String merchantId, String callbackUrl, String transactionId, String customerId) {
        IDGenerator generator = idGeneratorFactory.getIDGenerator(Order.TOKEN_ID);
        final String orderId = Order.TOKEN_ID + generator.getNextID();
		final Merchant merchant = merchantRepository.getByMerchantId(merchantId);
		Order order = new Order(orderId, serviceName, merchant, callbackUrl, transactionId, customerId);
		order = orderRepository.store(order);
		return order;
	}
}
