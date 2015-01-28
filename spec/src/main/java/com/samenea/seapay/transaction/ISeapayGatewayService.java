package com.samenea.seapay.transaction;

import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.bank.gateway.model.CommunicationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * SEAPAY local merchants can use this interface for communication with SEAPAY.
 * maybe SeapayGatewayService is better than ISeapayGatewayService.
 * @author soroosh sarabadani
 *
 */
@WebService
@Path("/")
@Produces({"application/json", "application/xml"})
public interface ISeapayGatewayService {
    /**
     * With this method merchant will be authenticated in SEAPAY and a session will be created for her.
     * Merchant can use sessionId for creating transactions, committing and ...
     * @param merchantId
     * @param serviceId
     * @param password
     * @return
     */
    @GET
    @Path("/createSession/{merchantId}/{serviceId}/{password}")
    public String createSession(@WebParam(name = "merchantId")  @PathParam("merchantId")   String merchantId,
                                @WebParam(name = "serviceId")   @PathParam("serviceId")    String serviceId,
                                @WebParam(name = "password")    @PathParam("password")     String password)
            ;

    /**
     * After creating session, merchant can use session for creating transaction with this method.
     *
     * @param sessionId return value of createSession
     * @param amount  amount of transaction.
     * @param description description about transaction.
     * @param callbackUrl after payment with client SEAPAY will redirect user to this page.
     * @param customerId
     * @return
     */
    @GET
    @Path("/createTransaction/{sessionId}/{amount}/{description}/{callbackUrl}/{customerId}")
    public String createTransaction(@WebParam(name = "sessionId")   @PathParam("sessionId")      String sessionId,
                                    @WebParam(name = "amount")      @PathParam("amount")         int amount,
                                    @WebParam(name = "description") @PathParam("description")    String description,
                                    @WebParam(name = "callbackUrl") @PathParam("callbackUrl")    String callbackUrl,
                                    @WebParam(name = "customerId")  @PathParam("customerId")     String customerId);

    /**
     * After user redirection, merchant must call this method.
     * This will be called if transaction receives a success result report by for example http redirect
     * based on that commitTransaction will be called to finalize transaction
     * @throws CommunicationException if can not communicate with underlying payment gateway this means
     * answer is not known and {@link #getTransactionStatus(String, String)} should be used to resolve the state
     * @throws NotFoundException if there is no transaction associated with session service
     * @throws AuthenticationException if session with this <code>sessionId</code> not found or transaction is not for
     * the service of this session ( transaction should be for the service of merchant specified by the session)
     * @throws IllegalStateException if transaction is in a state which can not be commited
     */
    @GET
    @Path("/commitTransaction/{sessionId}/{transactionId}/{amount}")
    @Transactional(propagation = Propagation.REQUIRED)
    public void commitTransaction(@WebParam(name = "sessionId")     @PathParam("sessionId")      String sessionId,
                                  @WebParam(name = "transactionId") @PathParam("transactionId")  String transactionId,
                                  @WebParam(name = "amount")        @PathParam("amount")         int amount)
    throws CommunicationException,AuthenticationException,NotFoundException,com.samenea.seapay.transaction.exceptions.IllegalStateException;

    /**
     * In the case of any unexpected failure for example network failure during transaction commit this method can
     * be used to examine transaction status and make appropriate action base on the result
     * @param sessionId sessionId of transaction
     * @param transactionId transaction id
     * @return status of specified transaction
     * @throws com.samenea.commons.component.model.exceptions.NotFoundException if the there is no transaction with this
     * id
     * @throws NotFoundException if there is no transaction associated with session service
     * @throws AuthenticationException if session with this <code>sessionId</code> not found or transaction is not for
     * the service of this session ( transaction should be for the service of merchant specified by the session)
     */
    @GET
    @Path("/getTransactionStatus/{sessionId}/{transactionId}")
    public TransactionStatus getTransactionStatus(@WebParam(name = "sessionId")     @PathParam("sessionId")       String sessionId,
                                                  @WebParam(name = "transactionId") @PathParam("transactionId")   String transactionId)
            throws AuthenticationException,NotFoundException;

    @GET
    @Path("/getBankName/{sessionId}/{transactionId}")
    public String getBankName(@WebParam(name = "sessionId") @PathParam("sessionId") String sessionId,
                              @WebParam(name = "transactionId") @PathParam("transactionId") String transactionId)
            throws AuthenticationException, NotFoundException;

}
