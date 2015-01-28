/**
 * Created with IntelliJ IDEA.
 * User: Soroosh Sarabadani
 * Date: 12/2/12
 * Time: 3:21 PM
 */
package com.samenea.seapay.session.repository;

import com.samenea.seapay.session.ISession;
import com.samenea.seapay.session.model.SessionRepository;
import com.samenea.seapay.transaction.model.repository.TransactionRepository;
import com.samenea.seapay.transaction.repository.TransactionBaseRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.annotation.DataSets;

/**
 * Created with IntelliJ IDEA.
 * User: soroosh
 * Date: 12/2/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionRepositoryHibernateTest extends TransactionBaseRepositoryTest {
    @Autowired
    private SessionRepository sessionRepository;

    private final String sessionId="S-100";

    @Test
    @DataSets(setUpDataSet = "/sample-data/session-exist-sample-data.xml")
    public void should_find_session_with_specified_sessionId(){
        ISession session = sessionRepository.getSessionById(sessionId);

        Assert.assertNotNull(session);
    }
}
