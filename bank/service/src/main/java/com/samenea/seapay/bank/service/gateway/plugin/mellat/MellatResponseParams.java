/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 10:58 AM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import org.springframework.util.Assert;

import java.util.Map;

/**
 * This class wraps the parameters that comes from payment request.
 * checking parameters is the responsibility of this class.
 */
public class MellatResponseParams {
    public final static String SALE_REFERENCE_ID = "SaleReferenceId";
    public final static String SALE_ORDER_ID = "SaleOrderId";

    private final Long saleReferenceId;
    private final Long saleOrderId;

    /**
     * @param params is the map of parameters that we need for commitTransaction request.
     */
    public MellatResponseParams(Map<String, String> params) {
        Assert.notNull(params, "Parameters cannot be null.");
        if (params.containsKey(SALE_REFERENCE_ID)) {
            final String refId = params.get(SALE_REFERENCE_ID);
            if (refId == null || refId.trim().equals("")) {
                this.saleReferenceId = 0L;
            } else {
                this.saleReferenceId = Long.parseLong(params.get(SALE_REFERENCE_ID));
            }
        } else {
            throw new NoMellatResponseException("Parameters does not have SALE_REFERENCE_ID.");
        }
        if (params.containsKey(SALE_ORDER_ID)) {
            this.saleOrderId = Long.parseLong(params.get(SALE_ORDER_ID));
        } else {
            throw new NoMellatResponseException("Parameters does not have SALE_ORDER_ID.");
        }

        if (this.saleReferenceId == null || this.saleOrderId == null)

        {
            throw new NoMellatResponseException("Parameters is not compatible with mellat.");
        }
    }

    public Long getSaleReferenceId() {
        return saleReferenceId;
    }

    public Long getSaleOrderId() {
        return saleOrderId;
    }

    @Override
    public String toString() {
        return "MellatParameterReader{" +
                "saleReferenceId=" + saleReferenceId +
                ", saleOrderId=" + saleOrderId +
                '}';
    }
}
