package com.samenea.seapay.bank.service.gateway.plugin.mellat.ws;

import com.samenea.commons.component.utils.Environment;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Date;

import static com.samenea.commons.component.utils.log.LoggerFactory.LoggerType.EXCEPTION;

@Service
public class MellatWSWrapper {
    Logger logger = LoggerFactory.getLogger(MellatWSWrapper.class);
    Logger exceptionLogger = LoggerFactory.getLogger(MellatWSWrapper.class, EXCEPTION);

    @Autowired
    private Environment environment;

    private IPaymentGateway gateway;
    private PaymentGatewayImplServiceLocator locator;

    public MellatWSWrapper() {
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        this.locator = new PaymentGatewayImplServiceLocator();
//        try {
//            gateway = this.locator.getPaymentGatewayImplPort();
//        } catch (ServiceException e) {
//            logger.error("Error in MellatWSWrapper Construction.");
//            exceptionLogger.error("Error in MellatWSWrapper Construction.",e);
//        }
    }

    public MellatWSResponse payRequest(AuthenticationData authenticationData, long orderId, long amount, String callbackUrl, String additionalData) {
        logger.trace("Mellat WebService bpPayRequest Called.");
        Date now = environment.getCurrentDate();
        final String wsResult;
        try {
            wsResult = getGateway().bpPayRequest(authenticationData.getTerminalId(), authenticationData.getUsername(), authenticationData.getPassword(), orderId, amount, buildDate(now), buildTime(now), additionalData, callbackUrl, 0);
            logger.debug("result code of mellat bpPayRequest: {}", wsResult);
            return new MellatWSResponse(wsResult);
        } catch (RemoteException e) {
            throw new CommunicationException("could not call mellat payRequest.", e);
        }


    }

    public MellatWSResponse commitTransaction(AuthenticationData authenticationData, long orderId, long saleOrderId, long saleReferenceId) {
        logger.trace("Mellat WebService bpVerifyRequest Called.");
        try {
            final String wsResult = getGateway().bpVerifyRequest(authenticationData.getTerminalId(), authenticationData.getUsername(), authenticationData.getPassword(), orderId, saleOrderId, saleReferenceId);
            logger.debug("result code of mellat bpVerifyRequest: {}", wsResult);
            return new MellatWSResponse(wsResult);
        } catch (RemoteException e) {
            throw new CommunicationException("could not call mellat commitTransaction.", e);
        }
    }

    public MellatWSResponse settle(AuthenticationData authenticationData, long orderId, long saleOrderId, long saleReferenceId) {
        logger.trace("Mellat WebService bpSettleRequest Called.");
        try {
            final String wsResult = getGateway().bpSettleRequest(authenticationData.getTerminalId(), authenticationData.getUsername(), authenticationData.getPassword(), orderId, saleOrderId, saleReferenceId);
            logger.debug("result code of mellat bpVerifyRequest: {}", wsResult);
            return new MellatWSResponse(wsResult);
        } catch (RemoteException e) {
            throw new CommunicationException("could not call mellat commitTransaction.", e);
        }

    }

    public MellatWSResponse check(AuthenticationData authenticationData, long orderId, long saleOrderId, long saleReferenceId) {
        logger.trace("Mellat WebService bpInquiryRequest Called.");
        try {
            final String wsResult = getGateway().bpInquiryRequest(authenticationData.getTerminalId(), authenticationData.getUsername(), authenticationData.getPassword(), orderId, saleOrderId, saleReferenceId);
            logger.debug("result code of mellat bpInquiryRequest: {}", wsResult);
            return new MellatWSResponse(wsResult);
        } catch (RemoteException e) {
            throw new CommunicationException("could not call mellat commitTransaction.", e);
        }
    }

    private IPaymentGateway getGateway() {
        if (this.gateway == null) {
            try {
                //todo: maybe retry is better here.
                this.gateway = this.locator.getPaymentGatewayImplPort();
            } catch (ServiceException e) {
                throw new CommunicationException("could not create mellat payment gateway webservice.", e);
            }
        }
        return this.gateway;
    }

    private String buildDate(Date date) {
        final String month = ("" + date.getMonth()).length() == 2 ? "" + date.getMonth() : "0" + date.getMonth();
        final String day = ("" + date.getDate()).length() == 2 ? "" + date.getDate() : "0" + date.getDate();
        return "" + (date.getYear() + 1900) + month + day;
    }

    private String buildTime(Date date) {
        return "" + date.getHours() + date.getMinutes();
    }
}
