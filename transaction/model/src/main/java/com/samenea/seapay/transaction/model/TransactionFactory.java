package com.samenea.seapay.transaction.model;


import com.samenea.commons.idgenerator.service.IDGenerator;
import com.samenea.commons.idgenerator.service.IDGeneratorFactory;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class TransactionFactory implements ITransactionFactory {
    @Autowired
    private IDGeneratorFactory idGeneratorFactory;

	@Autowired
	private TransactionRepository transactionRepository;
	
	/* (non-Javadoc)
	 * @see com.samenea.seapay.transaction.model.TransactionFactory#createTransaction(int, java.lang.String)
	 */
	@Override
	public ITransaction createTransaction(int amount, String description) {
        IDGenerator generator = idGeneratorFactory.getIDGenerator(Transaction.ID_TOKEN);
		String transactionTokenId = Transaction.ID_TOKEN + generator.getNextID();
		Transaction transaction = new Transaction(transactionTokenId, amount, description);
		transaction = transactionRepository.store(transaction);
		return transaction;
	}
}
