package com.samenea.seapay.merchant.service;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.model.Merchant;
import com.samenea.seapay.merchant.model.Service;
import com.samenea.seapay.merchant.repository.MerchantRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author: Soroosh Sarabadani
 * Date: 12/31/12
 * Time: 1:06 PM
 */


public class MerchantServiceTest {

    @Spy
    @InjectMocks
    MerchantService merchantService;

    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private Merchant merchant;

    private final String password = "Password";
    private final String serviceId = "S-100";
    private String merchantId = "M-100";

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void isMerchantValid_should_return_true_if_pass_is_ok_and_service_exist() {
        when(merchantRepository.getByMerchantId(merchantId)).thenReturn(merchant);
        when(merchant.getPassword()).thenReturn(password);
        when(merchant.hasService(serviceId)).thenReturn(true);
        final boolean result = merchantService.isMerchantValid(merchantId, serviceId, password);
        Assert.assertTrue(result);
    }

    @Test
    public void isMerchantValid_should_return_false_if_pass_is_not_true() {
        when(merchant.getPassword()).thenReturn(password);
        when(merchantRepository.getByMerchantId(merchantId)).thenReturn(merchant);

        final boolean result = merchantService.isMerchantValid(merchantId, serviceId, password);
        Assert.assertFalse(result);
    }

    @Test(expected = NotFoundException.class)
    public void should_throw_exception_if_could_not_find_merchant() {
        when(merchantRepository.getByMerchantId(merchantId)).thenThrow(NotFoundException.class);

        merchantService.isMerchantValid(merchantId, serviceId, password);
    }
}
