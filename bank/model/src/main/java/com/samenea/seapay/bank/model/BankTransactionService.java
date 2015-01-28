package com.samenea.seapay.bank.model;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.*;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Jalal Ashrafi
 */
@Service
@Transactional(noRollbackFor = FraudException.class)
public class BankTransactionService implements IBankTransactionService {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private GatewayFinder gatewayFinder;
    @Autowired
    private BankTransactionInfoRepository bankTransactionInfoRepository;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(BankTransactionService.class);

    @Override
    public List<? extends BankInfo> customerBanks(String customerId) {
        return bankRepository.getCustomerBanks(customerId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IRedirectData startTransaction(String transactionId, IBankAccount bankAccount) throws GatewayNotFoundException {

        final ITransaction transaction = transactionService.getTransaction(transactionId);
        final Date lastUpdateDate = transaction.getLastUpdateDate();
        if(lastUpdateDate.before(minutesBefore(10))){
            transaction.cancel();
            throw new IllegalStateException("Bank Selection timed out for transaction: "+transactionId);

        }

        //Todo i think this should be done in transaction module if possible and dependency to transactionService and ITransaction should be dropped
        transaction.mapToBank(bankAccount);
        logger.debug("Destination account selected accountId: {}", bankAccount.getAccountNumber());
        final GatewayPlugin plugin = gatewayFinder.findByName(transaction.getBankName());
        final IRedirectData response = plugin.request(transaction);

        final BankTransactionInfo bankTransactionInfo = new BankTransactionInfo(transaction.getTransactionId(), transaction.getBankName());
        bankTransactionInfo.setReferenceCode(plugin.getReferenceCodeFromResponse(response.getParameters()));
        bankTransactionInfo.saveTransactionStartParams(response.getParameters());
        bankTransactionInfo.savePaymentResponseParams(response.getParameters());
        logger.debug("Transaction start response is: {}", response.toString());
        bankTransactionInfoRepository.store(bankTransactionInfo);

        return response;

    }

    private Date minutesBefore(int minutes) {
        return new Date(environment.getCurrentDate().getTime() - (minutes * 60 * 1000));
    }

    @Override
    public PaymentResponseCode interpretPaymentResponse(TransactionInfo transactionInfo, Map<String, String> parameters) {
        Assert.notNull(transactionInfo, "Transaction info can not be null");
        Assert.notNull(parameters, "parameters can not be null");
        final GatewayPlugin plugin = gatewayFinder.findByName(transactionInfo.getBankName());
        final BankTransactionInfo bankTransactionInfo = bankTransactionInfoRepository.findByTransactionId(transactionInfo.getTransactionId());
        bankTransactionInfo.setReferenceCode(plugin.getReferenceCodeFromResponse(parameters));
        bankTransactionInfo.savePaymentResponseParams(parameters);
        bankTransactionInfoRepository.store(bankTransactionInfo);

        final PaymentResponseCode responseCode = plugin.interpretResponse(transactionInfo, parameters);
/*        if (responseCode != PaymentResponseCode.PAYMENT_OK) {
            ((ITransaction) transactionInfo).cancel();
        }*/
        return responseCode;
    }


    @Override
    public boolean commitTransaction(TransactionInfo transactionInfo) {
        final BankTransactionInfo associatedBankTransactionInfo = bankTransactionInfoRepository.
                findByTransactionId(transactionInfo.getTransactionId());
        final GatewayPlugin plugin = gatewayFinder.findByName(transactionInfo.getBankName());
        return plugin.verify(transactionInfo, associatedBankTransactionInfo.getPaymentResponseParams());
    }

    @Override
    public boolean pollTransaction(TransactionInfo transactionInfo) {
        final BankTransactionInfo associatedBankTransactionInfo = bankTransactionInfoRepository.findByTransactionId(transactionInfo.getTransactionId());
        final GatewayPlugin plugin = gatewayFinder.findByName(transactionInfo.getBankName());

        return plugin.isCommited(transactionInfo, associatedBankTransactionInfo);
    }

}
