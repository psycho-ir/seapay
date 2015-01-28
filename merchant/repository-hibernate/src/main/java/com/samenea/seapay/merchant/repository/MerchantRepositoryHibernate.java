package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.merchant.model.Merchant;

@Repository
public class MerchantRepositoryHibernate extends BasicRepositoryHibernate<Merchant, Long> implements MerchantRepository {

	public MerchantRepositoryHibernate() {
		super(Merchant.class);
	}

	@Override
	public Merchant getByMerchantId(String merchantId) {
		Query query = getSession().createQuery("from " + Merchant.class.getName() + " where merchantId=:merchantId ");
		query.setParameter("merchantId", merchantId);
		Object result = query.uniqueResult();
		if (result == null) {
			throw new NotFoundException("Merchant with id: " + merchantId + " does not exist.");
		}
		return (Merchant) result;
	}

}
