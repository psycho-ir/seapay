package com.samenea.seapay.merchant.service;

import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.samenea.seapay.merchant.model.Merchant;
import com.samenea.seapay.merchant.model.Order;
import com.samenea.seapay.merchant.model.Service;
import com.samenea.seapay.merchant.repository.OrderRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@org.springframework.stereotype.Service
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public Service findServiceOfTransaction(String transactionId) {
        logTransactionAvailability();
        Service service = null;
        final Order order = orderRepository.getByTransactionId(transactionId);
        if (order == null) {
            throw new IllegalArgumentException("Order does not exist. Transaction id:" + transactionId);
        }
        final Merchant merchant = order.getMerchant();

        if (merchant != null) {
            service = merchant.findService(order.getServiceName());
        }
        if (service == null) {
            final String errorMessage = "Service does not exist. Order id:" + order.getOrderId();
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        return service;
    }

    @Override
    @Transactional
    public IOrder findOrderByTransactionId(String transactionId) {
        logTransactionAvailability();
        return orderRepository.getByTransactionId(transactionId);
    }
    private void logTransactionAvailability() {
        if (logger.isDebugEnabled()) {
            logger.debug("Is transaction active? {}", TransactionSynchronizationManager.isActualTransactionActive());
        }
    }

}
