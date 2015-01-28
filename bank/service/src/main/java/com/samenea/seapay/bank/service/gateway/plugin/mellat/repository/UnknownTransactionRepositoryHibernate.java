package com.samenea.seapay.bank.service.gateway.plugin.mellat.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransaction;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.model.UnknownTransactionStatus;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/13/13
 * Time: 6:43 PM
 */
@Repository
public class UnknownTransactionRepositoryHibernate extends BasicRepositoryHibernate<UnknownTransaction, Long> implements UnknowTransactionRepository {
    public UnknownTransactionRepositoryHibernate() {
        super(UnknownTransaction.class);
    }

    public UnknownTransaction findByTransactionId(String transactionId) {
        Query query = getSession().createQuery("from " + UnknownTransaction.class.getName() + " where transactionId = :transactionId");
        query.setParameter("transactionId", transactionId);
        UnknownTransaction transaction = (UnknownTransaction) query.uniqueResult();
        if (transaction == null) {
            throw new NotFoundException(String.format("UnknownTransaction with transactionId:%s not found.", transactionId));
        }
        return transaction;


    }

    public List<UnknownTransaction> findByStatus(UnknownTransactionStatus status) {
        Query query = getSession().createQuery("from " + UnknownTransaction.class.getName() + " where status = :status");
        query.setParameter("status", status);
        return query.list();

    }


}
