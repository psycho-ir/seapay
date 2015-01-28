package com.samenea.seapay.bank.service.gateway.plugin.saman.ws;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by soroosh on 6/16/14.
 */
@Service
public class SamanWSWrapper {

    private final Logger exceptionLogger = LoggerFactory.getLogger(SamanWSWrapper.class, LoggerFactory.LoggerType.EXCEPTION);
    private final Logger logger = LoggerFactory.getLogger(SamanWSWrapper.class);

    PaymentIFBindingSoapStub s;

    public SamanWSWrapper() {
        try {
            logger.info("Creating saman webservice client.");
            s = new PaymentIFBindingSoapStub(new URL("https://sep.shaparak.ir/payments/referencepayment.asmx"), new org.apache.axis.client.Service());
        } catch (Exception e) {
            exceptionLogger.error("Error in creating saman web service client", e);
        }
    }


    public SamanWSResponse verify(final String referenceNumber, final String mid) {
        try {
            logger.info("Verifying transaction with referencenumber:{} MID:{}", referenceNumber, mid);
            double verifyResult = s.verifyTransaction(referenceNumber, mid);
            logger.info("Transaction with referencenumber:{} MID:{} verified by result:{}", referenceNumber, mid, verifyResult);
            return new SamanWSResponse(verifyResult);
        } catch (RemoteException e) {
            throw new CommunicationException("could not call saman verify.", e);
        }
    }
}
