package com.samenea.seapay.merchant.service;

import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IMerchant;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.merchant.IService;
import com.samenea.seapay.merchant.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MerchantService implements IMerchantService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public boolean isBankAndAccountNumberValid(String serviceId, String bankId, String accountNumber) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMerchantValid(String merchantId, String serviceName, String password) {
        final IMerchant merchant = this.merchantRepository.getByMerchantId(merchantId);
        return merchant.getPassword().equals(password) && merchant.hasService(serviceName);
    }

    @Override
    public IMerchant getMerchant(String merchantId) {
        return merchantRepository.getByMerchantId(merchantId);
    }

    @Override
    public List<String> findBanks(String transactionId) {
        System.out.println("Transaction Status is:" + TransactionSynchronizationManager.isActualTransactionActive());
        List<String> bankNames = new ArrayList<String>();
        final com.samenea.seapay.merchant.model.Service service = orderService.findServiceOfTransaction(transactionId);
        List<IBankAccount> accounts = service.getBankAccounts();
        for (IBankAccount account : accounts) {
            bankNames.add(account.getBankName());
        }

        return bankNames;
    }

}
