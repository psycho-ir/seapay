package com.samenea.seapay.bank.model;

import com.samenea.seapay.bank.gateway.model.CommunicationException;
import com.samenea.seapay.bank.gateway.model.GatewayNotFoundException;
import com.samenea.seapay.bank.gateway.model.IRedirectData;
import com.samenea.seapay.bank.gateway.model.TransactionStartException;
import com.samenea.seapay.merchant.IBankAccount;
import com.samenea.seapay.transaction.TransactionInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>Transaction workflow methods to start interpret and commit a transaction on a bank
 * the flow is as follows :
 * <ol>
 * <li>{@link #startTransaction(String, com.samenea.seapay.merchant.IBankAccount)} to start a new transaction and
 * redirecting by the provided info on bank if start transaction is successful
 * </li>
 * <li>
 * {@link #interpretPaymentResponse(com.samenea.seapay.transaction.TransactionInfo, java.util.Map)} see method docs
 * </li>
 * <li>
 * {@link #commitTransaction(com.samenea.seapay.transaction.TransactionInfo)} finalizing transaction
 * </li>
 * </ol>
 * </p>
 * {@link GatewayNotFoundException} will be thrown if there is no gateway for handling the transaction
 *
 * @author Jalal Ashrafi
 */
public interface IBankTransactionService {
    //todo maybe should be moved to another interface
    List<? extends BankInfo> customerBanks(String customerId);


    /**
     *  will find the status of transaction and return it.
     * @param transactionInfo
     */
    boolean pollTransaction(TransactionInfo transactionInfo);


    /**
     * starting a transaction and returning redirect data for redirecting to bank page
     *
     * @param transactionId transaction string
     * @return payment response if transaction started
     * @throws GatewayNotFoundException  if there is no gateway associated with this
     * @throws TransactionStartException if transaction can not be start now //todo be more specific
     * @throws CommunicationException    If there is any connection problem to the underlying payment gateway
     * @see IRedirectData
     */
    IRedirectData startTransaction(String transactionId, IBankAccount bankAccount) throws TransactionStartException, CommunicationException;

    /**
     * If transaction is successful it will return {@link PaymentResponseCode#PAYMENT_OK} else the result shows the specific
     * problem
     *
     * @param transactionInfo transaction associated with this
     * @param parameters      this parameters are bank specific params which usually redirected from bank gateway
     *                        after user enters payment info or cancels or ...
     * @return true if parameters show a successful payment
     * @throws CommunicationException
     * @see PaymentResponseCode to specific repsonses and their meaning
     */
    PaymentResponseCode interpretPaymentResponse(TransactionInfo transactionInfo, Map<String, String> parameters);

    /**
     * @param transactionInfo to be committed transaction info
     * @return transaction commit result
     * @throws CommunicationException if there is a problem connecting to the underlying payment gateway client should
     *                                poll transaction status
     */
    boolean commitTransaction(TransactionInfo transactionInfo) throws CommunicationException;
}
