package com.samenea.seapay.session.model;

import java.io.Serializable;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.seapay.session.ISession;

public interface SessionRepository extends BasicRepository<Session, Long> {
    ISession getSessionById(String sessionId);

}
