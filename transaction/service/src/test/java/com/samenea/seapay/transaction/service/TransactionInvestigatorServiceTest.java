package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.transaction.ITransaction;
import com.samenea.seapay.transaction.ITransactionService;
import com.samenea.seapay.transaction.TransactionStatus;
import com.samenea.seapay.transaction.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author: Soroosh Sarabadani
 * Date: 2/6/13
 * Time: 11:19 AM
 */

public class TransactionInvestigatorServiceTest {
    private Logger logger = LoggerFactory.getLogger(TransactionInvestigatorServiceTest.class);

    @InjectMocks
    private TransactionInvestigatorService transactionInvestigatorService;
    @Mock
    private Environment environment;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private Transaction transactionSelectable;
    @Mock
    private Transaction transactionNonSelectable;


    private static final Date date = Calendar.getInstance().getTime();
    private List<ITransaction> transactions = new ArrayList<ITransaction>();


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Date newDate = new Date(date.getTime() - (1000 * 60 * 60L));

        when(transactionNonSelectable.getStartDate()).thenReturn(date);
        when(transactionNonSelectable.getTransactionId()).thenReturn("TRN-1");
        when(transactionSelectable.getTransactionId()).thenReturn("TRN-2");
        when(transactionSelectable.getStartDate()).thenReturn(newDate);
        transactions.add(transactionSelectable);
        transactions.add(transactionNonSelectable);
        when(environment.getCurrentDate()).thenReturn(date);
        when(transactionService.findTransactionsByStatus(TransactionStatus.UNKNOWN)).thenReturn((List) transactions);
    }

    @Test
    public void should_investigate_transaction_that_their_age_is_more_than_threshold() {
        transactionInvestigatorService.runInvestigation();
        verify(transactionSelectable).investigate();
        verify(transactionNonSelectable, times(0)).investigate();

    }
}
