package com.samenea.seapay.web.util;

import com.samenea.seapay.bank.utils.model.CutOffItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/19/13
 * Time: 6:21 PM
 */
@Service
public class SaderatExcelReader extends ExcelReader<SaderatExcelRowConverter, CutOffItem> {
    private Logger logger = LoggerFactory.getLogger(SaderatExcelReader.class);
    private static final SaderatExcelRowConverter converter = new SaderatExcelRowConverter();

    @Override
    public SaderatExcelRowConverter getConverter() {
        return converter;
    }
}
