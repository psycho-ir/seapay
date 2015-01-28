package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.seapay.bank.gateway.model.RedirectData;
import com.samenea.seapay.bank.service.gateway.plugin.mellat.ws.MellatWSResponse;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class RedirectDataBuilder {
	public static final String REFID ="RefId";
	private MellatWSResponse mellatWSResponse;
	private RedirectData redirectData;
	private String mellatWSUrl ="https://bpm.shaparak.ir/pgwchannel/startpay.mellat";

	public RedirectDataBuilder(MellatWSResponse response) {
        Assert.notNull(response,"response should not be null");
        Assert.isTrue(response.isOK(),"response should be ok");
		this.mellatWSResponse = response;
	}

	public RedirectData createPaymentResponse() {
        this.redirectData = RedirectData.createForPost(mellatWSUrl, createParameters());
        return this.redirectData;
	}

	private Map<String,String> createParameters() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(REFID, mellatWSResponse.getReferenceId());
        return result;
	}

}
