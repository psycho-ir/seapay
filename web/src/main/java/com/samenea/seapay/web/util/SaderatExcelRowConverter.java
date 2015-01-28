package com.samenea.seapay.web.util;

import com.samenea.seapay.bank.utils.model.CutOffItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/19/13
 * Time: 5:23 PM
 */

public class SaderatExcelRowConverter implements ExcelRowConverter<CutOffItem> {
    private Logger logger = LoggerFactory.getLogger(SaderatExcelRowConverter.class);

    @Override
    public CutOffItem convert(Row row) {
        String terminal = "";
        String pan = "";
        String merchant = "";
        String installDate = "";
        String transactionTime = "";
        String referNumber = "";
        String description = "";
        String amount = "";
        String follow = "";
        String bank = "";
        final Iterator<Cell> cells = row.iterator();
        while (cells.hasNext()) {
            final Cell currentCell = cells.next();
            currentCell.setCellType(Cell.CELL_TYPE_STRING);
            final String value = currentCell.getStringCellValue();
            switch (currentCell.getColumnIndex()) {
                case 0:
                    terminal = value;
                    break;
                case 1:
                    pan = value;
                    break;
                case 2:
                    merchant = value;
                    break;
                case 3:
                    installDate = value;
                    break;
                case 4:
                    transactionTime = value;
                    break;
                case 5:
                    referNumber = value;
                    break;
                case 6:
                    description = value;
                    break;
                case 7:
                    amount = value;
                    break;
                case 8:
                    follow = value;
                    break;
                case 9:
                    bank = value;
                    break;

            }
        }
        CutOffItem cutOffItem = new CutOffItem(terminal, merchant, installDate, transactionTime, referNumber, description, Long.valueOf(amount), follow, bank, pan);
        return cutOffItem;

    }
}
