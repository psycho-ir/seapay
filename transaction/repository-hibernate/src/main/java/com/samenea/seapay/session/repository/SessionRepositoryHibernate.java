package com.samenea.seapay.session.repository;


import com.samenea.commons.component.model.exceptions.NotFoundException;
import com.samenea.seapay.session.ISession;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.samenea.commons.model.repository.BasicRepositoryHibernate;
import com.samenea.seapay.session.model.Session;
import com.samenea.seapay.session.model.SessionRepository;

@Repository
public class SessionRepositoryHibernate extends BasicRepositoryHibernate<Session, Long> implements SessionRepository {

	public SessionRepositoryHibernate() {
		super(Session.class);
	}

    @Override
    public ISession getSessionById(String sessionId) {
        final Query query = getSession().createQuery("from com.samenea.seapay.session.model.Session where  sessionId=:sessionId");
        query.setParameter("sessionId",sessionId);
        ISession session = (ISession) query.uniqueResult();
        if(session == null){
            throw new NotFoundException(String.format("Session with id %s does not exist.",sessionId));
        }
        return session;
    }
}
