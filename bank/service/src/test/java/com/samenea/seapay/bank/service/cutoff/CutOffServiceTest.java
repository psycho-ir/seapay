package com.samenea.seapay.bank.service.cutoff;

import com.samenea.commons.component.model.exceptions.DuplicateException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.service.BankBaseIntegrationServiceTest;
import com.samenea.seapay.bank.utils.model.CutOffItem;
import junit.framework.Assert;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author: Jalal Ashrafi
 * Date: 6/19/13
 */

public class CutOffServiceTest extends BankBaseIntegrationServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CutOffServiceTest.class);
    @Autowired
    CutOffService cutOffService;
    @Autowired
    SessionFactory sessionFactory;

    @Test
    public void testCreateCutOffItem_should_create_a_valid_item() throws Exception {
        final CutOffItem cutOffItem = new CutOffItem("terminalCode", "merchantCode", "installTransaction", "transationTime"
                , "referNumber", "transactionDescription", 10000, "followCode", "bank", "pan");
        final Boolean aBoolean = cutOffService.saveCutOffItem(cutOffItem);
        Assert.assertTrue(aBoolean);
    }

    @Test
    public void testCreateCutOffItem_should_not_create_with_duplicate_referNumber() throws Exception {
        final String duplicateReferNumber = "duplicateReferNumber";
        final CutOffItem cutOffItem = new CutOffItem("terminalCode", "merchantCode", "installTransaction", "transationTime"
                , duplicateReferNumber, "transactionDescription", 10000, "followCode", "bank", "pan");
        cutOffService.saveCutOffItem(cutOffItem);
        sessionFactory.getCurrentSession().flush();
        final CutOffItem duplicate = new CutOffItem("1terminalCode", "1merchantCode", "1installTransaction", "1transationTime"
                , duplicateReferNumber, "1transactionDescription", 10000, "1followCode", "1bank", "1pan");
        final Boolean aBoolean = cutOffService.saveCutOffItem(duplicate);
//        sessionFactory.getCurrentSession().flush();
        System.out.println(aBoolean);
        Assert.assertFalse(aBoolean);

    }
}
