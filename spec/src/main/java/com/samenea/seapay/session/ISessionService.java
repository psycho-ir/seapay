package com.samenea.seapay.session;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 12/2/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISessionService {
    /**
     *
     * @param sessionId sessionId
     * @return found session with sessionId
     * @throws com.samenea.commons.component.model.exceptions.NotFoundException if there is no session with this sessionId
     */
    ISession getSession(String sessionId);
    ISession createSession(String merchantId, String serviceId, String password) throws RequestNotValidException;
    List<? extends ISession> getAll();
}
