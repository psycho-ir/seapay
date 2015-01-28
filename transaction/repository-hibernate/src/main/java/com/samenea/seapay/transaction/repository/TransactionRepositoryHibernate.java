package com.samenea.seapay.transaction.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.Transaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryHibernate extends BasicRepositoryHibernate<Transaction, String> implements TransactionRepository {
    public TransactionRepositoryHibernate() {
        super(Transaction.class);
    }

    @Override
    public Transaction getByTransactionId(String transactionId) {

        Query query = getSession().createQuery("from " + Transaction.class.getName() + " where transactionId =:transactionId");
        query.setParameter("transactionId", transactionId);

        final Transaction transaction = (Transaction) query.uniqueResult();
        if (transaction == null) {
            throw new NotFoundException(String.format("Transaction with id: %s not found", transactionId));
        }
        return transaction;
    }

    @Override
    public List<Transaction> getByTransactionStatus(TransactionStatus status) {
        Query query = getSession().createQuery("from " + Transaction.class.getName() + " where status = :status");
        query.setParameter("status", status);
        return query.list();
    }
}
