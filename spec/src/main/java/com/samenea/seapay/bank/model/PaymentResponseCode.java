package com.samenea.seapay.bank.model;

/**
 * @author Jalal Ashrafi
 * Payment response returned from bank after callback from bank. Bank response code will be mapped to this response
 * codes by payment gateway plugin.
 */
public enum PaymentResponseCode {
    /**
     * Payment is done correctly ( but is  not finalized yet )
     */
    PAYMENT_OK,
    /**
     * User cancelled payment
     */
    CANCELED_BY_USER,
    /**
     * When the gateway is busy
      */
    TIME_OUT,
    CARD_EXPIRED
}
