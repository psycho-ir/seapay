package com.samenea.seapay.bank.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.BankTransactionInfoRepository;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Jalal Ashrafi
 */
@Repository
public class BankTransactionInfoRepositoryHibernate extends BasicRepositoryHibernate<BankTransactionInfo,Long> implements BankTransactionInfoRepository {
    private BankTransactionInfoRepositoryHibernate() {
        super(BankTransactionInfo.class);
    }

    @Override
    public BankTransactionInfo findByTransactionId(String transactionId) {
        final Query query = getSession().createQuery("from BankTransactionInfo where transactionId = :transactionId");
        query.setParameter("transactionId",transactionId);

        final BankTransactionInfo bankTransactionInfo = (BankTransactionInfo) query.uniqueResult();
        if(bankTransactionInfo == null){
            throw new NotFoundException(String.format("No bank transaction info for transactionId: %s Found.", transactionId));
        }
        return bankTransactionInfo;
    }
}
