package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.log.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/26/13
 * Time: 2:05 PM
 */

public class SaderatTransactionTrackingResponse {
    public static final String COMMITED = "1";
    public static final String PENDING = "4";
    private Logger logger = LoggerFactory.getLogger(SaderatTransactionTrackingResponse.class);

    public Map<String, String> getParameters() {
        return parameters;
    }

    Map<String, String> parameters = new HashMap<String, String>();

    public SaderatTransactionTrackingResponse(String commitedResult) {
        final String[] splitedResult = commitedResult.split("&");
        for (int i = 0; i < splitedResult.length; i++) {
            final String[] keyValue = splitedResult[i].split("=");
            parameters.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
        }
    }

    public Boolean isCommited() {
        return parameters.get(SaderatParams.X_RESPONSE_CODE).trim().equals(COMMITED);
    }

    public boolean isPending() {
        return parameters.get(SaderatParams.X_RESPONSE_CODE).trim().equals(PENDING);
    }
}
