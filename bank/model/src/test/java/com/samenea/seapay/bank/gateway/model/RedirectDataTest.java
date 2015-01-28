package com.samenea.seapay.bank.gateway.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.samenea.seapay.bank.gateway.model.IRedirectData.HttpMethod.POST;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * @author Jalal Ashrafi
 */

public class RedirectDataTest {
    final String url = "http://google.com";
    Map<String,String> parameters;


    @Before
    public void setup(){
        parameters = new HashMap<String, String>();
        parameters.put("key", "value");
    }
    @Test
    public void createForPost_should_create_response_with_post_as_http_method(){
        final RedirectData redirectData = RedirectData.create(url, parameters, POST);
        assertEquals(url, redirectData.getUrl());
        assertEquals(POST, redirectData.getHttpMethod());
        assertEquals(parameters, redirectData.getParameters());

        //Changing parameter should not have side effect on paymentResponse params
        parameters.put("key2","value2");
        assertFalse(parameters.equals(redirectData.getParameters()));

    }
    @Test(expected = IllegalArgumentException.class)
    public void create_should_not_allow_null_url(){
        RedirectData.createForPost(null, parameters);
    }
    @Test(expected = IllegalArgumentException.class)
    public void create_should_not_allow_null_httpMethod(){
        RedirectData.create(url, parameters,null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void create_should_not_allow_mallformed_url(){
        final String malformedUrl = "httpd/:google.com";
        RedirectData.createForPost(malformedUrl, parameters);
    }
    @Test(expected = IllegalArgumentException.class)
    public void create_should_not_allow_null_params(){
        final String url = "http://google.com";
        RedirectData.createForPost(url, null);
    }
}
