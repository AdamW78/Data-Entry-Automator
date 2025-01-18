package io.input;

import io.Headers;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class XlsxHeaderReader {
    public static Headers readHeaders(XSSFSheet sheet) {
        Headers headers = new Headers();
        int rowZeroWidth = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < rowZeroWidth; i++) {
            String header = sheet.getRow(0).getCell(i).getStringCellValue();
            headers.addHeader(header, i);
        }
        return headers;
    }
}
