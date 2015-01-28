package com.samenea.seapay.merchant.model;

import com.samenea.commons.component.model.Entity;
import com.samenea.commons.component.model.exceptions.DuplicateException;
import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.merchant.IMerchant;
import com.samenea.seapay.merchant.IOrder;
import com.samenea.seapay.merchant.IService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.*;


/**
 * @author Jalal Ashrafi
 */
@javax.persistence.Entity
@Configurable
public class Merchant extends Entity<Long> implements IMerchant {
    @Autowired
    @Transient
    private OrderFactory orderFactory;
    @Column(nullable = false)
    private final String merchantId;
    @Column(nullable = false)
    private final String password;

    private String title;
    @ElementCollection(targetClass = BankAccount.class)
    private final Set<BankAccount> bankAccounts = new HashSet<BankAccount>();
    @OneToMany
    private final Set<Service> services = new HashSet<Service>();

    private static final Logger logger = LoggerFactory.getLogger(Merchant.class);

    private Merchant() {
        this.merchantId = null;
        this.password = null;

    }

    public Merchant(String merchantId, String password) {
        Assert.notNull(merchantId, "merchantId cannot be null.");
        Assert.hasText(merchantId, "merchantId cannot be empty.");
        Assert.notNull(password, "password cannot be null.");
        Assert.hasText(password, "password cannot be empty.");

        this.merchantId = merchantId;
        this.password = password;
    }

    public String getMerchantId() {
        return merchantId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    public void addService(String serviceName) {
        if (!services.add(new Service(serviceName))) {
            throw new DuplicateException(String.format("This merchant %s already has a service with this number :%s"
                    , merchantId, serviceName), null);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param bankAccount should not be null
     * @throws DuplicateException if a BankAccount with this bank account number already exist
     */
    public void addBankAccount(BankAccount bankAccount) {
        if (!bankAccounts.add(bankAccount)) {
            throw new DuplicateException(String.format("This merchant %s already has account with this number :%s"
                    , merchantId, bankAccount.getAccountNumber()), null);
        }
    }

    public List<BankAccount> getBankAccounts() {
        return Collections.unmodifiableList(new ArrayList<BankAccount>(bankAccounts));
    }

    @Override
    public List<? extends IService> getServices() {
        return Collections.unmodifiableList(new ArrayList<Service>(services));
    }

    @Override
    public boolean hasService(String serviceName) {
        Assert.hasText(serviceName, "Service can not be null or empty.");
        for (IService service : services) {
            if (service.getServiceName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public void assignAccountToService(String serviceName, String accountNumber) {
        Service foundService = findService(serviceName);
        BankAccount foundAccount = findAccount(accountNumber);
        foundService.addAccount(foundAccount);
    }

    public Service findService(String serviceName) {
        Service foundService = null;
        for (Service service : services) {
            if (service.getServiceName().equals(serviceName)) {
                foundService = service;
            }
        }
        if (foundService == null) {
            throw new NotFoundException(String.format("There is no service with name: %s for merchant: %s", serviceName, merchantId));
        }
        return foundService;
    }

    private BankAccount findAccount(String accountNumber) {
        BankAccount foundAccount = null;
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                foundAccount = bankAccount;
            }
        }
        if (foundAccount == null) {
            throw new NotFoundException(String.format("Account with this number: %s can not be found for merchant: %s", accountNumber, merchantId));
        }
        return foundAccount;
    }

	@Override
	public IOrder createOrder(String serviceName, String transactionId, String callbackUrl, String customerId) {
        final Order order = orderFactory.createOrder(serviceName, this.merchantId, callbackUrl, transactionId, customerId);
        logger.debug("Order created: {}",order.toString());
        return order;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((merchantId == null) ? 0 : merchantId.hashCode());
		result = prime * result + ((services == null) ? 0 : services.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Merchant other = (Merchant) obj;
		if (merchantId == null) {
			if (other.merchantId != null)
				return false;
		} else if (!merchantId.equals(other.merchantId))
			return false;

		return true;
	}

    @Override
    public String toString() {
        return "Merchant{" +
                "merchantId='" + merchantId + '\'' +
                ", password='" + password + '\'' +
                ", title='" + title + '\'' +
                ", services=" + services +
                ", bankAccounts=" + bankAccounts +
                '}';
    }
}
