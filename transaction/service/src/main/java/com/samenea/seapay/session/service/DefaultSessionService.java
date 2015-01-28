package com.samenea.seapay.session.service;

import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.ISessionFactory;
import com.samenea.seapay.session.ISessionService;
import com.samenea.seapay.session.RequestNotValidException;
import com.samenea.seapay.session.model.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service

public class DefaultSessionService implements ISessionService {
    private final Logger logger = LoggerFactory.getLogger(DefaultSessionService.class);
    @Autowired
    private ISessionFactory sessionFactory;
    @Autowired
    private SessionRepository sessionRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ISession createSession(String merchantId, String serviceId, String password) throws RequestNotValidException {
        if (logger.isDebugEnabled()) {
            logger.debug("Is transaction active? {}", TransactionSynchronizationManager.isActualTransactionActive());
        }
        return sessionFactory.createSession(merchantId, serviceId, password);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ISession getSession(String sessionId) {
        return sessionRepository.getSessionById(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends ISession> getAll() {
        return sessionRepository.getAll();
    }


}
