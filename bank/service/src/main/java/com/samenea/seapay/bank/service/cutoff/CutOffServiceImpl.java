package com.samenea.seapay.bank.service.cutoff;

import com.samenea.commons.component.model.BasicRepository;
import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.utils.model.CutOffItem;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

/**
 * @author: Jalal Ashrafi
 * Date: 6/19/13
 */
@Service
public class CutOffServiceImpl implements CutOffService {
    private static final Logger logger = LoggerFactory.getLogger(CutOffServiceImpl.class);
    @Autowired
    @Qualifier("cutOffItemRepository")
    private BasicRepository<CutOffItem, Long> cutOffItemBasicRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean saveCutOffItem(CutOffItem cutOffItem) {
        try {
            sessionFactory.getCurrentSession().setFlushMode(FlushMode.MANUAL);
            final Query query = sessionFactory.getCurrentSession().createQuery(MessageFormat.format("from {0} where referNumber = :referNumber",CutOffItem.class.getName()));
            query.setParameter("referNumber", cutOffItem.getReferNumber());
            final int size = query.list().size();
            if (size == 0) {

                cutOffItemBasicRepository.store(cutOffItem);
                sessionFactory.getCurrentSession().flush();

                return true;
            }
            return false;
        } catch (Exception e) {
            logger.info("Error in saving cutoff",e);
            return false;
        }
    }
}
