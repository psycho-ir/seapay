package com.samenea.seapay.bank.service.cutoff;

import com.samenea.commons.component.utils.log.LoggerFactory;
import com.samenea.seapay.bank.utils.model.CutOffItem;
import org.slf4j.Logger;

/**
 * @author: Jalal Ashrafi
 * Date: 6/19/13
 */
public interface CutOffService {
    public Boolean saveCutOffItem(CutOffItem cutOffItem);
}
