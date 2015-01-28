/**
 * User: Soroosh Sarabadani
 * Date: 12/24/12
 * Time: 2:53 PM
 */
package com.samenea.seapay.merchant.repository;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.model.Merchant;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.annotation.DataSets;

public class MerchantRepositoryHibernateTest extends MerchantBaseRepositoryTest {
    @Autowired
    private MerchantRepository merchantRepository;

    @Test(expected = NotFoundException.class)
    public void should_throw_exception_when_merchant_does_not_exist() {
        merchantRepository.getByMerchantId("111");
    }

    @Test
    @DataSets(setUpDataSet = "/sample-data/merchant-with-service-exists-sample-data.xml")
    public void should_find_merchant_with_given_id() {
        Merchant merchant = merchantRepository.getByMerchantId("M-100");

        Assert.assertEquals(new Long(-1), merchant.getId());
        Assert.assertEquals("title", merchant.getTitle());

    }

}
