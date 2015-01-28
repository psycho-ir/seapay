package com.samenea.seapay.client;


import com.samenea.seapay.client.impl.SeapayGatewayWebServiceImpl;
import com.samenea.seapay.client.ws.AuthenticationException_Exception;
import com.samenea.seapay.client.ws.CommunicationException_Exception;
import com.samenea.seapay.client.ws.IllegalStateException_Exception;
import com.samenea.seapay.client.ws.NotFoundException_Exception;
import org.junit.Ignore;
import org.junit.Test;

//currently it is used just in the case of manual testing by commenting igonre when needed
@Ignore()
public class SeapayGatewayWebserviceTest {
    @Test
    public void testCreateSession() throws AuthenticationException_Exception, IllegalStateException_Exception, CommunicationException_Exception, NotFoundException_Exception {
        final SeapayGatewayWebServiceImpl seapayGatewayWS = new SeapayGatewayWebServiceImpl("http://localhost:8181/web/services/SeapayGatewayService?wsdl");
        final String sessionID = seapayGatewayWS.createSession("M-100", "S-100", "123456");
        final String transactionId = seapayGatewayWS.createTransaction(sessionID, 10, "t", "http://google.com", "jalala");
        seapayGatewayWS.commitTransaction(sessionID, transactionId,10);

        System.out.println("session is " + sessionID);
        System.out.println("transaction is " + transactionId);
//        System.out.println("state1 is " + seapayGatewayWS.getTransactionStatus(sessionID,"TRN-1"));
        System.out.println("state2 is " + seapayGatewayWS.getTransactionStatus(sessionID,transactionId));
    }
}
