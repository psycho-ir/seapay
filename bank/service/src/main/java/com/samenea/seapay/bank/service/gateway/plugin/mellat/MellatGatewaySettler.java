/**
 * User: Soroosh Sarabadani
 * Date: 12/25/12
 * Time: 12:04 PM
 */
package com.samenea.seapay.bank.service.gateway.plugin.mellat;


import com.samenea.commons.component.utils.command.Command;
import com.samenea.commons.component.utils.command.Retry;
import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSWrapper;
import org.springframework.util.Assert;

class MellatGatewaySettler {

    private final MellatWSWrapper mellatWSWrapper;
    private final MellatGatewayConfigurationReader configurationReader;
    private final MellatResponseParams parameterReader;

    public MellatGatewaySettler(MellatWSWrapper mellatWSWrapper, MellatGatewayConfigurationReader configurationReader, MellatResponseParams parameterReader) {
        this.configurationReader = configurationReader;
        this.parameterReader = parameterReader;

        Assert.notNull(mellatWSWrapper, "mellatWSWrapper cannot be null.");
        this.mellatWSWrapper = mellatWSWrapper;
    }

    public MellatWSResponse settle() {

        Command<MellatWSResponse> settle = new Command<MellatWSResponse>() {
            @Override
            public MellatWSResponse execute() {
                return mellatWSWrapper.settle(configurationReader.getAuthenticationData(), parameterReader.getSaleOrderId(), parameterReader.getSaleOrderId(), parameterReader.getSaleReferenceId());
            }

            @Override
            public String toString() {
                return "Retry Settle";
            }
        };
        return Retry.on(settle).maxRetry(10).waitForEachRetry(100L).execute();
    }
}
