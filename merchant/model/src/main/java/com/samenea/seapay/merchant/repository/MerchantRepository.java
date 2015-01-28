package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.seapay.merchant.model.Merchant;
import com.samenea.seapay.merchant.model.Order;

public interface MerchantRepository extends BasicRepository<Merchant, Long> {

	Merchant getByMerchantId(String merchantId);

}
