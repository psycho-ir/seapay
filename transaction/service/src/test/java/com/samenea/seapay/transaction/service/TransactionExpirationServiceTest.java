package com.samenea.seapay.transaction.service;

import com.samenea.commons.component.utils.log.LoggerFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Jalal Ashrafi
 * Date: 10/13/13
 */
public class TransactionExpirationServiceTest extends TransactionBaseServiceTest{
    private static final Logger logger = LoggerFactory.getLogger(TransactionExpirationServiceTest.class);

    @Autowired
    TransactionExpirationService transactionExpirationService;

    @Test
    public void testRun() throws Exception {
        transactionExpirationService.run();


    }
}
