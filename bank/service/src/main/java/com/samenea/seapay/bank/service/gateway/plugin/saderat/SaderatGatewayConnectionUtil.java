package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Copy pasted from saderat document for connecting to the system :D
 *
 * @author: Soroosh Sarabadani
 * Date: 1/24/13
 * Time: 2:11 PM
 */

@Service
public class SaderatGatewayConnectionUtil {
    private Logger logger = LoggerFactory.getLogger(SaderatGatewayConnectionUtil.class);

    public String request(String pathURL) throws IOException,
            KeyManagementException, NoSuchAlgorithmException {
        logger.info("Request {} sent to Saderat Gateway.", pathURL);
        String content = "";
        try {

            URL url = new URL(pathURL);

            TrustManager[] trustAllCerts = new TrustManager[]
                    {
                            new X509TrustManager() {
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return null;
                                }

                                public void
                                checkClientTrusted(java.security.cert.X509Certificate[] certs, String
                                        authType) {
                                }

                                public void
                                checkServerTrusted(java.security.cert.X509Certificate[] certs, String
                                        authType) {
                                }
                            }
                    };


            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            javax.net.ssl.HostnameVerifier hv = new javax.net.ssl.HostnameVerifier() {
                public boolean verify(String urlHostname, SSLSession ssls) {
                    System.out.println("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };

            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HttpsURLConnection https_conn = (HttpsURLConnection) url.openConnection();

            https_conn.setRequestMethod("POST");
            https_conn.setRequestProperty("Content-type", "text/plain");
            https_conn.setDoOutput(true);
            https_conn.setDoInput(true);
            https_conn.setUseCaches(false);
            https_conn.setConnectTimeout(2*60000);
            https_conn.setReadTimeout(2*60000);
            OutputStreamWriter out = new
                    OutputStreamWriter(https_conn.getOutputStream());
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(https_conn.
                    getInputStream()));
            String str;
            while ((str = in.readLine()) != null) {
                content += str;
            }
            in.close();
        } catch (MalformedURLException e) {
            throw new IOException(
                    "Exception in communicating with merchant in URL Simulator because of : \n" +
                            e.getLocalizedMessage());
        } catch (IOException e) {
            throw new IOException(
                    "Exception in communicating with merchant in URL Simulator because of : \n" +
                            e.getLocalizedMessage());
        }
        logger.info("Response {} received from Saderat Gateway.", content);
        return content;
    }

    public String request(String saderatVerificationUrl, Map<String, String> parameters) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        String result = "?";
        for (String key : parameters.keySet()) {
            String param = key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8");
            if (result.equals("?")) {
                result += param;
            } else {
                result += "&" + param;
            }
        }
        return request(saderatVerificationUrl + result);

    }
}
