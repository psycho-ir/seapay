/**
 * Created with IntelliJ IDEA.
 * User: Soroosh Sarabadani
 * Date: 11/14/12
 * Time: 9:16 AM
 */
package com.samenea.seapay.bank.repository;

import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.bank.model.BankAccount;
import com.samenea.seapay.bank.model.BankAccountRepository;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 11/14/12
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BankAccountRepositoryHibernate extends BasicRepositoryHibernate<BankAccount, Long> implements BankAccountRepository {

    public BankAccountRepositoryHibernate() {
        super(BankAccount.class);
    }

    @Override
    public BankAccount getBankAccountByAccountNumber(String accountNumber) {
        Query query = getSession().createQuery(MessageFormat.format("from {0} where accountNumber=:accountNumber", BankAccount.class.getName()));
        query.setParameter("accountNumber",accountNumber);
        return (BankAccount)query.uniqueResult();
    }
}
