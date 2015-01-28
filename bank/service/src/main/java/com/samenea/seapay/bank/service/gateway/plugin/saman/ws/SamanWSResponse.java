package com.samenea.seapay.bank.service.gateway.plugin.saman.ws;

/**
 * Created by soroosh on 6/16/14.
 */
public class SamanWSResponse {
    private final double verifyCode;

    public SamanWSResponse(double verifyCode) {
        this.verifyCode = verifyCode;
    }

    public boolean isOK() {
        if (this.verifyCode > 0) {
            return true;
        }
        return false;
    }

    public double getVerifyCode() {
        return this.verifyCode;
    }
}
