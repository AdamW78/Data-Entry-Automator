package org.awdevelopment.smithlab.io.input;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.awdevelopment.smithlab.io.Headers;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.io.exceptions.InvalidDayNumberException;
import org.awdevelopment.smithlab.io.exceptions.InvalidHeadersException;
import org.awdevelopment.smithlab.logging.LoggerHelper;


public class XlsxHeaderReader {

    public static Headers readHeaders(LoggerHelper LOGGER, XSSFSheet sheet) throws InvalidHeadersException {
        Headers headers = new Headers();
        int rowZeroWidth = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < rowZeroWidth; i++) {
            XSSFCell cell = sheet.getRow(0).getCell(i);
            CellType cellType = cell.getCellType();
            if (cellType == CellType._NONE || cellType == CellType.BLANK || cellType == CellType.ERROR) {
                LOGGER.atWarn("Cell in header row at zero-indexed column "+i
                        + " has type \"_NONE\", \"BLANK\", or \"ERROR\", skipping...");
            } else if (cellType == CellType.NUMERIC) {
                short dayNumber;
                try { dayNumber = (short) cell.getNumericCellValue(); }
                catch (ClassCastException c) { throw new InvalidDayNumberException(c, cell.getNumericCellValue()); }
                headers.addDay(dayNumber, i);
            }
            else if (cellType == CellType.STRING) {
                String headerCellContents = cell.getStringCellValue();
                headers.addHeader(headerCellContents, i);
            }
        }
        if (!headers.checkHeaders()) {
            throw new InvalidHeadersException();
        }
        return headers;
    }
}
