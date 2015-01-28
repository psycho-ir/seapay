
package com.samenea.seapay.client.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.samenea.seapay.client.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTransactionStatus_QNAME = new QName("http://transaction.seapay.samenea.com/", "getTransactionStatus");
    private final static QName _CommitTransaction_QNAME = new QName("http://transaction.seapay.samenea.com/", "commitTransaction");
    private final static QName _GetBankName_QNAME = new QName("http://transaction.seapay.samenea.com/", "getBankName");
    private final static QName _AuthenticationException_QNAME = new QName("http://transaction.seapay.samenea.com/", "AuthenticationException");
    private final static QName _GetBankNameResponse_QNAME = new QName("http://transaction.seapay.samenea.com/", "getBankNameResponse");
    private final static QName _NotFoundException_QNAME = new QName("http://transaction.seapay.samenea.com/", "NotFoundException");
    private final static QName _CommitTransactionResponse_QNAME = new QName("http://transaction.seapay.samenea.com/", "commitTransactionResponse");
    private final static QName _GetTransactionStatusResponse_QNAME = new QName("http://transaction.seapay.samenea.com/", "getTransactionStatusResponse");
    private final static QName _CreateSessionResponse_QNAME = new QName("http://transaction.seapay.samenea.com/", "createSessionResponse");
    private final static QName _CommunicationException_QNAME = new QName("http://transaction.seapay.samenea.com/", "CommunicationException");
    private final static QName _CreateSession_QNAME = new QName("http://transaction.seapay.samenea.com/", "createSession");
    private final static QName _CreateTransactionResponse_QNAME = new QName("http://transaction.seapay.samenea.com/", "createTransactionResponse");
    private final static QName _IllegalStateException_QNAME = new QName("http://transaction.seapay.samenea.com/", "IllegalStateException");
    private final static QName _CreateTransaction_QNAME = new QName("http://transaction.seapay.samenea.com/", "createTransaction");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.samenea.seapay.client.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateTransactionResponse }
     * 
     */
    public CreateTransactionResponse createCreateTransactionResponse() {
        return new CreateTransactionResponse();
    }

    /**
     * Create an instance of {@link CreateSession }
     * 
     */
    public CreateSession createCreateSession() {
        return new CreateSession();
    }

    /**
     * Create an instance of {@link CommunicationException }
     * 
     */
    public CommunicationException createCommunicationException() {
        return new CommunicationException();
    }

    /**
     * Create an instance of {@link IllegalStateException }
     * 
     */
    public IllegalStateException createIllegalStateException() {
        return new IllegalStateException();
    }

    /**
     * Create an instance of {@link CreateTransaction }
     * 
     */
    public CreateTransaction createCreateTransaction() {
        return new CreateTransaction();
    }

    /**
     * Create an instance of {@link CommitTransaction }
     * 
     */
    public CommitTransaction createCommitTransaction() {
        return new CommitTransaction();
    }

    /**
     * Create an instance of {@link GetBankName }
     * 
     */
    public GetBankName createGetBankName() {
        return new GetBankName();
    }

    /**
     * Create an instance of {@link GetTransactionStatus }
     * 
     */
    public GetTransactionStatus createGetTransactionStatus() {
        return new GetTransactionStatus();
    }

    /**
     * Create an instance of {@link GetBankNameResponse }
     * 
     */
    public GetBankNameResponse createGetBankNameResponse() {
        return new GetBankNameResponse();
    }

    /**
     * Create an instance of {@link AuthenticationException }
     * 
     */
    public AuthenticationException createAuthenticationException() {
        return new AuthenticationException();
    }

    /**
     * Create an instance of {@link CommitTransactionResponse }
     * 
     */
    public CommitTransactionResponse createCommitTransactionResponse() {
        return new CommitTransactionResponse();
    }

    /**
     * Create an instance of {@link NotFoundException }
     * 
     */
    public NotFoundException createNotFoundException() {
        return new NotFoundException();
    }

    /**
     * Create an instance of {@link CreateSessionResponse }
     * 
     */
    public CreateSessionResponse createCreateSessionResponse() {
        return new CreateSessionResponse();
    }

    /**
     * Create an instance of {@link GetTransactionStatusResponse }
     * 
     */
    public GetTransactionStatusResponse createGetTransactionStatusResponse() {
        return new GetTransactionStatusResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransactionStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "getTransactionStatus")
    public JAXBElement<GetTransactionStatus> createGetTransactionStatus(GetTransactionStatus value) {
        return new JAXBElement<GetTransactionStatus>(_GetTransactionStatus_QNAME, GetTransactionStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "commitTransaction")
    public JAXBElement<CommitTransaction> createCommitTransaction(CommitTransaction value) {
        return new JAXBElement<CommitTransaction>(_CommitTransaction_QNAME, CommitTransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "getBankName")
    public JAXBElement<GetBankName> createGetBankName(GetBankName value) {
        return new JAXBElement<GetBankName>(_GetBankName_QNAME, GetBankName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "AuthenticationException")
    public JAXBElement<AuthenticationException> createAuthenticationException(AuthenticationException value) {
        return new JAXBElement<AuthenticationException>(_AuthenticationException_QNAME, AuthenticationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "getBankNameResponse")
    public JAXBElement<GetBankNameResponse> createGetBankNameResponse(GetBankNameResponse value) {
        return new JAXBElement<GetBankNameResponse>(_GetBankNameResponse_QNAME, GetBankNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "NotFoundException")
    public JAXBElement<NotFoundException> createNotFoundException(NotFoundException value) {
        return new JAXBElement<NotFoundException>(_NotFoundException_QNAME, NotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "commitTransactionResponse")
    public JAXBElement<CommitTransactionResponse> createCommitTransactionResponse(CommitTransactionResponse value) {
        return new JAXBElement<CommitTransactionResponse>(_CommitTransactionResponse_QNAME, CommitTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransactionStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "getTransactionStatusResponse")
    public JAXBElement<GetTransactionStatusResponse> createGetTransactionStatusResponse(GetTransactionStatusResponse value) {
        return new JAXBElement<GetTransactionStatusResponse>(_GetTransactionStatusResponse_QNAME, GetTransactionStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateSessionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "createSessionResponse")
    public JAXBElement<CreateSessionResponse> createCreateSessionResponse(CreateSessionResponse value) {
        return new JAXBElement<CreateSessionResponse>(_CreateSessionResponse_QNAME, CreateSessionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommunicationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "CommunicationException")
    public JAXBElement<CommunicationException> createCommunicationException(CommunicationException value) {
        return new JAXBElement<CommunicationException>(_CommunicationException_QNAME, CommunicationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateSession }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "createSession")
    public JAXBElement<CreateSession> createCreateSession(CreateSession value) {
        return new JAXBElement<CreateSession>(_CreateSession_QNAME, CreateSession.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "createTransactionResponse")
    public JAXBElement<CreateTransactionResponse> createCreateTransactionResponse(CreateTransactionResponse value) {
        return new JAXBElement<CreateTransactionResponse>(_CreateTransactionResponse_QNAME, CreateTransactionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalStateException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "IllegalStateException")
    public JAXBElement<IllegalStateException> createIllegalStateException(IllegalStateException value) {
        return new JAXBElement<IllegalStateException>(_IllegalStateException_QNAME, IllegalStateException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://transaction.seapay.samenea.com/", name = "createTransaction")
    public JAXBElement<CreateTransaction> createCreateTransaction(CreateTransaction value) {
        return new JAXBElement<CreateTransaction>(_CreateTransaction_QNAME, CreateTransaction.class, null, value);
    }

}
