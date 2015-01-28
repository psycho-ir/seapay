package com.samenea.seapay.merchant.model;

import com.samenea.commons.component.model.exceptions.DuplicateException;
import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.merchant.model.BankAccount;
import com.samenea.seapay.merchant.model.Merchant;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jalal Ashrafi
 */
public class MerchantTest {
	private Merchant merchant;
	private final String bankName = "bankName";
    private final String password="pass";
    private String merchantId = "M-100";

    @Before
	public void setUp() {
		merchant = new Merchant("test",password);
	}

	@Test
	public void testConstructor() {
		final String merchantId = "merchantId";
		// final String merchantPassword = "merchantPassword";
		Merchant merchant = new Merchant(merchantId,password);
		assertTrue(merchant.getBankAccounts().isEmpty());
		assertTrue(merchant.getServices().isEmpty());
		assertEquals(merchantId, merchant.getMerchantId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorIllegalArgEmptyMerchantId() {
		new Merchant("",password);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorIllegalArgNull() {
		new Merchant(null,password);
	}

    @Test(expected =  IllegalArgumentException.class)
    public void constructor_should_throw_exception_if_password_is_empty(){
        new Merchant(merchantId, "");
    }


    @Test(expected =  IllegalArgumentException.class)
    public void constructor_should_throw_exception_if_password_is_null(){
        new Merchant(merchantId, null);
    }

	@Test
	public void testSetMerchantTitle() {
		Merchant merchant = new Merchant("test",password);
		merchant.setTitle("title");
		assertEquals("title", merchant.getTitle());
	}

	@Test
	public void addAccount_should_add_account_to_merchant_account_list() {
		merchant.addBankAccount(new BankAccount("accountNumber", "title", bankName));
		assertEquals(1, merchant.getBankAccounts().size());
	}

	@Test(expected = DuplicateException.class)
	public void addAccount_does_not_accept_duplicate() {
		merchant.addBankAccount(new BankAccount("accountNumber", "title", bankName));
		merchant.addBankAccount(new BankAccount("accountNumber", "title", bankName));
	}

	@Test
	public void testAddService() {
		final String serviceName = "ServiceName";
		merchant.addService(serviceName);
		assertEquals(1, merchant.getServices().size());
		assertEquals(serviceName, merchant.getServices().get(0).getServiceName());
		merchant.addService("ServiceName2");
		assertEquals(2, merchant.getServices().size());
	}

	@Test(expected = DuplicateException.class)
	public void testAddServiceDuplicate() {
		merchant.addService("ServiceName");
		merchant.addService("ServiceName");
	}

	@Test
	public void testAssignAccountToService() {
		// setup
		String serviceName = "serviceName";
		merchant.addService(serviceName);
		BankAccount bankAccount = new BankAccount("121254", "title", bankName);
		merchant.addBankAccount(bankAccount);

		// act
		merchant.assignAccountToService(serviceName, bankAccount.getAccountNumber());

		// verify
		assertEquals(1, merchant.getServices().get(0).getBankAccounts().size());
		assertEquals(bankAccount, merchant.getServices().get(0).getBankAccounts().get(0));
	}

	@Test(expected = NotFoundException.class)
	public void testAssignAccountToServiceAccountNotFound() {
		String serviceName = "serviceName";
		merchant.addService(serviceName);

		// act
		merchant.assignAccountToService(serviceName, "accountNumber");

	}

	@Test()
	public void testAssignAccountToServiceMultipleAssignmentHasNoEffect() {
		// setup
		String serviceName = "serviceName";
		merchant.addService(serviceName);
		BankAccount bankAccount = new BankAccount("121254", "title", bankName);
		merchant.addBankAccount(bankAccount);

		// act
		merchant.assignAccountToService(serviceName, bankAccount.getAccountNumber());
		merchant.assignAccountToService(serviceName, bankAccount.getAccountNumber());

		// verify
		assertEquals(1, merchant.getServices().get(0).getBankAccounts().size());
		assertEquals(bankAccount, merchant.getServices().get(0).getBankAccounts().get(0));
	}

	@Test(expected = NotFoundException.class)
	public void testAssignAccountToServiceServiceNotFound() {
		String serviceName = "serviceName";
		BankAccount bankAccount = new BankAccount("121254", "title", bankName);
		merchant.addBankAccount(bankAccount);

		// act
		merchant.assignAccountToService(serviceName, bankAccount.getAccountNumber());
	}
    @Test
    public void hasService_should_return_true_for_a_merchant_service(){
        final String serviceName = "aService";
        merchant.addService(serviceName);
        assertTrue("Merchant has this service!", merchant.hasService(serviceName));
    }
    @Test
    public void hasService_should_return_false_for_a_not_existing_service(){
        assertFalse("Merchant does not have this service!", merchant.hasService("notExistingService"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void hasService_does_not_accept_null(){
        merchant.hasService(null);
    }
    @Test
    public void merchants_with_same_merchantId_and_different_attributes_are_equals_(){
        final Merchant anotherMerchant = new Merchant(merchant.getMerchantId(), "different pass");
        merchant.addService("aService");
        merchant.addBankAccount(new BankAccount("123","title","bank"));

        anotherMerchant.addService("diffService");
        anotherMerchant.addBankAccount(new BankAccount("132","diffTitle","diffBank2"));

        Assert.assertEquals(merchant, anotherMerchant);
    }
    @Test
    public void merchants_with_same_attributes_and_different_merchantId_are_not_equals(){
        final Merchant anotherMerchant = new Merchant("differentMID", merchant.getPassword());
        Assert.assertFalse(merchant.equals(anotherMerchant));
    }

}
