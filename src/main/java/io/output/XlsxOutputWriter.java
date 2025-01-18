package io.output;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxOutputWriter {

    public void writeOutput(String outputFileName) throws OutputException {
        File outputFile = new File(outputFileName);
        try (XSSFWorkbook workbook = new XSSFWorkbook(outputFile)) {
            XSSFSheet[] outputSheets = outputStyle.generateOutputSheets();
            for (XSSFSheet sheet : outputSheets) {
                workbook.createSheet(sheet.getSheetName());
            }
            writeWorkbookToFile(outputFile, workbook);
        } catch (IOException e) {
            throw new OutputException(outputFileName, e);
        }
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            fileOut.flush();
            workbook.write(fileOut);
        } catch (IOException e) {
           throw new OutputException(outputFileName, e);
    }
}
