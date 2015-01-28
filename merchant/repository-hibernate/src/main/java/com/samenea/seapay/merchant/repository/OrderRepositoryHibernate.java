package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.merchant.model.Merchant;
import com.samenea.seapay.merchant.model.Order;

@Repository
public class OrderRepositoryHibernate extends BasicRepositoryHibernate<Order, Long> implements OrderRepository {

	public OrderRepositoryHibernate() {
		super(Order.class);
	}

	@Override
	public Order getByTransactionId(String transactionId) {
		Query query = getSession().createQuery("from " + Order.class.getName() + " where transactionId =:transactionId");
		query.setParameter("transactionId", transactionId);
        final Order order = (Order) query.uniqueResult();
        if(order == null){
            throw new NotFoundException(String.format("No Order for transactionId: %s Found.", transactionId));
        }
        return order;
	}
}
