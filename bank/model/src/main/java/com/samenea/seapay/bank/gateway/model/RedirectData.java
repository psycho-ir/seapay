package com.samenea.seapay.bank.gateway.model;

import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for returning parameters of startTransaction response for every plugin
 * @see GatewayPlugin#request(com.samenea.seapay.transaction.TransactionInfo)
 */
public class RedirectData implements IRedirectData {

    private final Map<String, String> parameters = new HashMap<String, String>();
	private final String url;
    private final HttpMethod httpMethod;

    /**
     * @param url the url
     * @param params key value maps to be sent on bank payment page
     * @param method http method
     * @throws IllegalArgumentException if any of parameters are null or url is a malformed url
     */
    private RedirectData(String url, Map<String, String> params, HttpMethod method) {
        validateUrl(url);
        validateParameters(params);
        Assert.notNull(method,"Http method can not be null.");
        this.parameters.putAll(params);
        this.url = url;
        this.httpMethod = method;
    }

    private void validateParameters(Map<String, String> params) {
        Assert.notNull(params, "Params can not be null.");
    }

    private RedirectData(String url, Map<String, String> params) {
        this(url, params, HttpMethod.POST);
    }


    @Override
    public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}


    /**
     * How redirect should be done.
     */
    @Override
    public HttpMethod getHttpMethod() {
		return httpMethod;
	}

    /**
     * To which url redirect should be done
     * @return
     */
	@Override
    public String getUrl() {
		return url;
	}

    public static RedirectData createForPost(String url, Map<String, String> params) {
        return new RedirectData(url, params);
    }

    public static RedirectData create(String url, Map<String, String> params, HttpMethod method) {
        return new RedirectData(url, params, method);
    }
    private void validateUrl(String url) {
        Assert.notNull(url, "url can not be null");
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return "RedirectData{" +
                "url='" + url + '\'' +
                ", httpMethod=" + httpMethod +
                ", parameters=" + parameters.toString() +
                '}';
    }
}
