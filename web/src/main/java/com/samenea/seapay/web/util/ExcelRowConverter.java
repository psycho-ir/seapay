package com.samenea.seapay.web.util;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/19/13
 * Time: 5:23 PM
 */
public interface ExcelRowConverter<T> {
    public T convert(Row row);
}
