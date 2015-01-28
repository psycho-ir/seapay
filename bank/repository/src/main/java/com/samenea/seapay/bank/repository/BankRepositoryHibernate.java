package com.samenea.seapay.bank.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.bank.model.Bank;
import com.samenea.seapay.bank.model.BankRepository;
import com.samenea.seapay.bank.model.Ibank;

@Repository
public class BankRepositoryHibernate extends BasicRepositoryHibernate<Bank, Long> implements BankRepository {

	public BankRepositoryHibernate() {
		super(Bank.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Ibank> getCustomerBanks(String customerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
