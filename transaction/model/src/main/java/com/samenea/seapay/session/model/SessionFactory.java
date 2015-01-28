package com.samenea.seapay.session.model;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.merchant.IMerchantService;
import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionFactory;
import com.samenea.seapay.session.RequestNotValidException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Configurable
public class SessionFactory implements ISessionFactory {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    @Transactional
    public ISession createSession(String merchantId, String serviceId, String password) throws RequestNotValidException {
        if (!merchantService.isMerchantValid(merchantId, serviceId, password)) {
            logger.debug("Session creation with merchantId:{} serviceId:{} password:{} is not valid.", merchantId, serviceId, password);
            throw new RequestNotValidException("Session creation request is not valid.");
        }
        final String sessionId = createId();
        Session createdSession = new Session(merchantId, sessionId, serviceId);
        createdSession = sessionRepository.store(createdSession);
        logger.info("New payment session created. sessionId: {}",createdSession.toString());
        return createdSession;

    }

    private String createId() {
        return UUID.randomUUID().toString();
    }

}
