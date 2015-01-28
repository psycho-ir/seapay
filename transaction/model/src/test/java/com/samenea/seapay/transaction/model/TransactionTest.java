package com.samenea.seapay.transaction.model;

import com.samenea.commons.component.utils.Environment;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.model.IBankTransactionService;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.transaction.TransactionInfo;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;

public class TransactionTest extends TransactionBaseModelTest {

    private final Long transactionNumber = 123L;
    private final String transactionId = "TRN-" + transactionNumber;
    private final int amount = 900;
    private final String description = "description";
    private final String bankName = "mellat";
    private final String accountNumber = "1111";
    @Autowired
    private Environment environment;
    @Autowired
    private IMerchantService service;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private IBankTransactionService bankTransactionService;
    @Mock
    IBankAccount bankAccount;

    Transaction transaction;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(bankAccount.getAccountNumber()).thenReturn(accountNumber);
        when(bankAccount.getBankName()).thenReturn(bankName);
        when(service.isBankAndAccountNumberValid(anyString(), anyString(), anyString())).thenReturn(true);
        reset(transactionRepository);
        reset(bankTransactionService);
        when(bankTransactionService.commitTransaction(any(TransactionInfo.class))).thenReturn(true);

    }

    @Test
    public void should_create_object_with_new_status() {
        transaction = new Transaction(transactionId, amount, description);
        Assert.assertEquals(TransactionStatus.NEW, transaction.getStatus());
    }

    @Test
    public void should_create_transaction_with_associated_parameters() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);

        Assert.assertEquals(transactionId, transaction.getTransactionId());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(description, transaction.getDescription());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_transactionId_is_null() {
        transaction = new Transaction(null, amount, description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_transactionId_is_empty() {
        transaction = new Transaction(" ", amount, description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_description_is_null() {
        transaction = new Transaction(null, amount, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_description_is_empty() {
        transaction = new Transaction(transactionId, amount, " ");
    }

    @Test
    public void should_mapToBank_set_transactionAccount() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);

        Assert.assertEquals(bankName, transaction.getTransactionAccount().getBankName());
        Assert.assertEquals(accountNumber, transaction.getTransactionAccount().getAccountNumber());

    }

    @Test
    public void should_mapToBank_call_repository_store() {
        transaction = new Transaction(transactionId, amount, description);

        transaction.mapToBank(bankAccount);

        verify(transactionRepository).store(transaction);
    }

    @Test
    public void should_mapToBank_update_lastUpdateDate_update_status_to_BANK_RESOLVED() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);

        transaction.mapToBank(bankAccount);
        Assert.assertEquals(TransactionStatus.BANK_RESOLVED, transaction.getStatus());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test(expected = IllegalStateException.class)
    public void map_should_throw_exception_when_transaction_status_is_not_NEW() {
        transaction = new Transaction(transactionId, amount, description);

        transaction.mapToBank(bankAccount);
        transaction.mapToBank(bankAccount);

    }

    @Test(expected = IllegalStateException.class)
    public void markUnknown_should_throw_exception_when_status_is_not_BANK_RESOLVED() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.markUnknown();

    }

    @Test
    public void markUnknown_should_make_status_UNKNOWN_and_update_lastUpdateTime() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();

        Assert.assertEquals(TransactionStatus.UNKNOWN, transaction.getStatus());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test
    public void commit_should_make_status_COMMITED_and_update_lastUpdateTime() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.commit();

        Assert.assertEquals(TransactionStatus.COMMITED, transaction.getStatus());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test
    public void commit_should_call_repositry_store() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.commit();

        verify(transactionRepository, times(2)).store(any(Transaction.class));
    }

    @Test(expected = IllegalStateException.class)
    public void commit_should_throw_exception_when_current_state_is_NEW() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.commit();
    }

    @Test(expected = IllegalStateException.class)
    public void commit_should_throw_exception_when_current_state_is_FAILED() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.cancel();
        transaction.commit();
    }

    @Test
    public void commit_should_not_throw_exception_when_current_state_is_UNKNOWN() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();
        transaction.commit();

        Assert.assertEquals(TransactionStatus.COMMITED, transaction.getStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void commit_should_thow_exception_commitTransaction_returns_false() {
        when(bankTransactionService.commitTransaction(any(TransactionInfo.class))).thenReturn(false);
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();
        transaction.commit();

    }

    @Test
    public void rollback_should_make_status_FAILED_and_update_lastUpdateTime() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.cancel();

        Assert.assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test
    public void rollback_should_call_repository_store() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.cancel();

        verify(transactionRepository, times(2)).store(any(Transaction.class));
    }

    @Test(expected = IllegalStateException.class)
    public void rollback_should_throw_exception_when_current_status_is_COMMITED() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);

        transaction.mapToBank(bankAccount);
        transaction.commit();
        transaction.cancel();

        Assert.assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        Assert.assertEquals(date, transaction.getLastUpdateDate());
    }

    @Test
    public void bankMap_should_set_startDate() {
        Date date = mockEnvironmentCalendar();
        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);

        Assert.assertEquals(date, transaction.getStartDate());

    }

    @Test
    public void equals_should_return_false() {
        transaction = new Transaction(transactionId, amount, description);
        com.samenea.seapay.transaction.ITransaction differentTransaction = new Transaction("different", amount, description);

        Assert.assertFalse(transaction.equals(differentTransaction));
    }

    @Test
    public void equals_should_return_true() {
        transaction = new Transaction(transactionId, amount, description);
        com.samenea.seapay.transaction.ITransaction differentTransaction = new Transaction(transactionId, amount, description);

        Assert.assertTrue(transaction.equals(differentTransaction));
    }

    @Test
    public void should_return_true_transaction_number_correspond_to_transactionId() {
        transaction = new Transaction(transactionId, amount, description);

        Assert.assertEquals(transactionNumber.longValue(), transaction.getTransactionNumber());
    }

    @Test
    public void commit_should_call_bank_commit() {

        transaction = new Transaction(transactionId, amount, description);
        transaction.mapToBank(bankAccount);
        transaction.commit();

        verify(bankTransactionService).commitTransaction(transaction);
    }

    //investigate tests
    @Test(expected = IllegalStateException.class)
    public void inversigate_should_throw_exception_when_transaction_is_NEW() {
        transaction = new Transaction(transactionId, amount, description);

        transaction.investigate();
    }

    @Test(expected = IllegalStateException.class)
    public void inversigate_should_throw_exception_when_transaction_is_FAILED() {
        transaction = new Transaction(transactionId, amount, description);
        transaction.cancel();

        transaction.investigate();
    }

    @Test
    public void investigate_should_make_transaction_COMMITED_WITH_DELAY() {
        transaction = new Transaction(transactionId, amount, description);
        when(bankTransactionService.pollTransaction(transaction)).thenReturn(true);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();

        transaction.investigate();

        Assert.assertEquals(TransactionStatus.COMMITED_WITH_DELAY, transaction.getStatus());
    }

    @Test
    public void investigate_should_change_transaction_to_FAIELD_when_poll_returns_false() {
        transaction = new Transaction(transactionId, amount, description);
        when(bankTransactionService.pollTransaction(transaction)).thenReturn(false);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();

        transaction.investigate();

        Assert.assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    @Test
    public void investigate_should_not_change_transaction_state_when_poll_throws_exception() {
        transaction = new Transaction(transactionId, amount, description);
        when(bankTransactionService.pollTransaction(transaction)).thenThrow(CommunicationException.class);
        transaction.mapToBank(bankAccount);
        transaction.markUnknown();
        try {
            transaction.investigate();
        } catch (CommunicationException e) {
        }

        Assert.assertEquals(TransactionStatus.UNKNOWN, transaction.getStatus());
    }

    private Date mockEnvironmentCalendar() {
        reset(environment);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 10, 24);
        when(environment.getCurrentDate()).thenReturn(calendar.getTime());
        return calendar.getTime();
    }

}
