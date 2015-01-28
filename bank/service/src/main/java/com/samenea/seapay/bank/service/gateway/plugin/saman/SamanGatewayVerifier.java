package com.samenea.seapay.bank.service.gateway.plugin.saman;

import com.samenea.commons.component.utils.command.Command;
import com.samenea.commons.component.utils.command.Retry;
import com.samenea.seapay.bank.gateway.model.FraudException;
import com.samenea.seapay.bank.gateway.model.VerifyException;
import com.samenea.seapay.bank.service.gateway.plugin.saman.ws.SamanWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.saman.ws.SamanWSWrapper;
import com.samenea.seapay.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by soroosh on 6/16/14.
 */
public class SamanGatewayVerifier {
    private final SamanWSWrapper wsWrapper;
    private final Logger logger = LoggerFactory.getLogger(SamanGatewayVerifier.class);

    public SamanGatewayVerifier(SamanWSWrapper wsWrapper) {
        this.wsWrapper = wsWrapper;
    }

    public void verify(final String refNumber, final String mid, final TransactionInfo transaction) {
        final Command<SamanWSResponse> inquiry = new Command<SamanWSResponse>() {
            @Override
            public SamanWSResponse execute() {
                return wsWrapper.verify(refNumber, mid);
            }
        };

        try {

            SamanWSResponse response = wsWrapper.verify(refNumber, mid);
            logger.info("Transaction:{} Saman verify response: {}", transaction.getTransactionId(), response.getVerifyCode());
            throwExceptionWhenResponseIsNotOK(response,transaction);
        } catch (VerifyException e) {
            throw e;

        } catch (Exception e) {
            SamanWSResponse response = Retry.on(inquiry).maxRetry(10).waitForEachRetry(100L).execute();
            logger.info("Transaction:{} Saman inquiry response after error:{} response: {}", transaction.getTransactionId(), e.getMessage(), response.getVerifyCode());
            throwExceptionWhenResponseIsNotOK(response,transaction);
        }

    }

    private void throwExceptionWhenResponseIsNotOK(SamanWSResponse response, TransactionInfo transaction) {
        if (!response.isOK()) {
            throw new VerifyException(String.format("Error in Saman verification. Result Code:{0}", response.getVerifyCode()));
        } else if (transaction.getAmount() != response.getVerifyCode()) {
            throw new FraudException(String.format("Error in Saman verification. Amounts are not equal. expected:{0} actual:{1}", transaction.getAmount(), response.getVerifyCode()));
        }
    }


}
