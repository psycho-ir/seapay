/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 2:50 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.command.Command;
import com.samenea.commons.component.utils.command.Retry;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import org.slf4j.Logger;
import org.springframework.util.Assert;

public class MellatGatewayVerifier {

    private final MellatWSWrapper mellatWSWrapper;
    private final MellatGatewayConfigurationReader configReader;
    private final MellatResponseParams parameterReader;

    private static final Logger verifyLogger = LoggerFactory.getLogger(MellatGatewayVerifier.class);


    public MellatGatewayVerifier(MellatWSWrapper mellatWSWrapper, MellatGatewayConfigurationReader configReader, MellatResponseParams parameterReader) {
        this.configReader = configReader;
        this.parameterReader = parameterReader;
        Assert.notNull(mellatWSWrapper, "mellatWSWrapper cannot be null.");
        this.mellatWSWrapper = mellatWSWrapper;
    }


    public void verify() throws VerifyException {
        final long saleOrderId = parameterReader.getSaleOrderId();
        final long saleReferenceId = parameterReader.getSaleReferenceId();

        final Command<MellatWSResponse> inquiry = new Command<MellatWSResponse>() {
            @Override
            public MellatWSResponse execute() {
                return mellatWSWrapper.check(configReader.getAuthenticationData(), saleOrderId, saleOrderId, saleReferenceId);
            }
        };

        MellatWSResponse mellatWSResponse = null;
        try {

            mellatWSResponse = mellatWSWrapper.commitTransaction(configReader.getAuthenticationData(), saleOrderId, saleOrderId, saleReferenceId);
            verifyLogger.info("Transaction:{} Mellat verify response: {}", saleOrderId, mellatWSResponse.toString());
            throwExceptionWhenResponseIsNotOK(mellatWSResponse);
        } catch (VerifyException e) {
            throw e;

        } catch (Exception e) {
            mellatWSResponse = Retry.on(inquiry).maxRetry(10).waitForEachRetry(100L).execute();
            verifyLogger.info("Transaction:{} Mellat inquiry response after error:{} response: {}", saleOrderId, e.getMessage(), mellatWSResponse.toString());
            throwExceptionWhenResponseIsNotOK(mellatWSResponse);
        }
    }

    private void throwExceptionWhenResponseIsNotOK(MellatWSResponse mellatWSResponse) {
        if (!mellatWSResponse.isOK()) {
            throw new VerifyException(String.format("Error in Mellat verification. Result Code:{0}", mellatWSResponse.getResultCode()));
        }
    }
}
