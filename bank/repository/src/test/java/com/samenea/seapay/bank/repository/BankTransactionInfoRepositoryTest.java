package com.samenea.seapay.bank.repository;

import com.google.common.collect.Maps;
import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.bank.model.BankTransactionInfo;
import com.samenea.seapay.bank.model.BankTransactionInfoRepository;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Jalal Ashrafi
 */
@ContextConfiguration(locations = "classpath*:/context.xml")
public class BankTransactionInfoRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    BankTransactionInfoRepository bankTransactionInfoRepository;

    final String bankName = "mellat";
    final String transactionId = "123";
    @Test
    public void get_by_transaction_id_and_plugin_name_should_return_correct_transaction_info(){
        final BankTransactionInfo transactionInfo = bankTransactionInfoRepository.findByTransactionId(transactionId);
        assertNotNull(transactionInfo);
        Assert.assertEquals(bankName, transactionInfo.getBankName());
        Assert.assertEquals(transactionId, transactionInfo.getTransactionId());

    }
    //
    @Test(expected = DataIntegrityViolationException.class)
    public void should_not_be_able_to_save_duplicate_BankTransactionInfo(){
        BankTransactionInfo transactionInfo = new BankTransactionInfo(transactionId,bankName);
        bankTransactionInfoRepository.store(transactionInfo);
        BankTransactionInfo duplicate = new BankTransactionInfo(transactionId,bankName);
        bankTransactionInfoRepository.store(duplicate);

        //Ok, it is odd but its added to trigger a flush!!! by hibernate. sessionFactory.getCurrentSession().flush() does
        //not work because it does not translate exception to spring exception. it is just a hack
        bankTransactionInfoRepository.findByTransactionId(transactionId);
//        sessionFactory.getCurrentSession().flush();

    }
    @Test
    public void store_should_save_correctly(){
        BankTransactionInfo bt = new BankTransactionInfo("id","mellat");
        Map<String,String> map = Maps.newHashMap();
        map.put("jalala","jalalaa");
        bt.savePaymentResponseParams(map);
        bankTransactionInfoRepository.store(bt);
        final BankTransactionInfo byTransactionId = bankTransactionInfoRepository.findByTransactionId("id");
        assertEquals(bt,byTransactionId);
        assertEquals(bt.getPaymentResponseParams(), byTransactionId.getPaymentResponseParams());

    }
    @Test(expected = NotFoundException.class)
    public void get_by_transaction_id_and_plugin_name_should_throw_exception_when_not_exist_such_transaction_info(){
        final String bankName = "mellat";
        final String transactionId = "notExist";
        final BankTransactionInfo transactionInfo = bankTransactionInfoRepository.findByTransactionId(transactionId);
        assertNotNull(transactionInfo);
        Assert.assertEquals(bankName, transactionInfo.getBankName());
        Assert.assertEquals(transactionId, transactionInfo.getTransactionId());

    }
}
