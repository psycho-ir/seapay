package com.samenea.seapay.merchant;

import java.util.List;

public interface IMerchantService {
	boolean isBankAndAccountNumberValid(String serviceId,String bankId,String accountNumber);
	boolean isMerchantValid(String merchantId,String serviceId,String password);
	IMerchant getMerchant(String merchantId);
	List<String> findBanks(String transactionId);
	
}
