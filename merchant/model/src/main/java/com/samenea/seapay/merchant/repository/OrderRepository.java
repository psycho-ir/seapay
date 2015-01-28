package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.seapay.merchant.model.Order;

public interface OrderRepository extends BasicRepository<Order, Long> {

	Order getByTransactionId(String transactionId);

}
