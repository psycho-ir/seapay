package com.samenea.seapay.bank.service.gateway.plugin.saderat;

import com.samenea.commons.component.utils.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A Utility for hashing parameters, standard is from Saderat spec.
 *
 * @author: Soroosh Sarabadani
 * Date: 1/26/13
 * Time: 11:10 AM
 */
@Service
public class SaderatHashUtil {
    private Logger logger = LoggerFactory.getLogger(SaderatHashUtil.class);
    private final String hashAlgorithm = "HmacMD5";


    public String hash(String transactionKey, List<String> codesForHash) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(transactionKey.getBytes(), hashAlgorithm);

        Mac mac = Mac.getInstance(hashAlgorithm);
        mac.init(key);
        String inputstring = createHashString(codesForHash);
        mac.doFinal(inputstring.getBytes());
        byte[] result = mac.doFinal(inputstring.getBytes());

        StringBuffer strbuf = new StringBuffer(result.length * 2);
        for (int i = 0; i < result.length; i++) {
            if (((int) result[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) result[i] & 0xff, 16));
        }
        return strbuf.toString();

    }

    public String createRequestHashKey(String transactionKey, final String loginID, final String sequence, String timeStamp, String amount, String currency) throws InvalidKeyException, NoSuchAlgorithmException {
        final List<String> parameters = new ArrayList<String>();
        parameters.add(loginID);
        parameters.add(sequence);
        parameters.add(timeStamp);
        parameters.add(amount);
        parameters.add(currency);
        return this.hash(transactionKey, parameters);
    }

    private String createHashString(List<String> parameters) {
        return join(parameters, "^");

    }

    private String join(Collection<?> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }


}
