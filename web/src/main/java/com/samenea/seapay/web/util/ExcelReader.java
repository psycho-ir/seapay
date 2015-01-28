package com.samenea.seapay.web.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/19/13
 * Time: 5:19 PM
 */

public abstract class ExcelReader<T extends ExcelRowConverter<K>, K> {
    private Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    public abstract T getConverter();

    public List<K> convert(String fileAddress) throws IOException {
        FileInputStream file = new FileInputStream(new File(fileAddress));
        List<K> result = new ArrayList<K>();

        HSSFWorkbook workbook = new HSSFWorkbook(file);
        final HSSFSheet sheet = workbook.getSheetAt(0);
        final Iterator<Row> rows = sheet.iterator();
        rows.next();
        while (rows.hasNext()) {
            final Row currentRow = rows.next();
            result.add(getConverter().convert(currentRow));
            if (!currentRow.iterator().hasNext()) {
                break;
            }

        }
        return result;
    }
}
