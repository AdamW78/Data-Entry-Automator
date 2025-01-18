package io.output;

import formats.OutputStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxOutputWriter {

    private final OutputStyle outputStyle;

    public XlsxOutputWriter(OutputStyle outputStyle) {
        this.outputStyle = outputStyle;
    }

    public void writeOutput(String outputFileName) throws OutputException {
        File outputFile = new File(outputFileName);
        try (XSSFWorkbook workbook = new XSSFWorkbook(outputFile)) {
            outputStyle.generateOutputSheets(workbook);
            writeWorkbookToFile(outputFile, workbook);
        } catch (IOException | InvalidFormatException e) {
            throw new OutputException(outputFileName, e);
        }
    }

    private void writeWorkbookToFile(File outputFile, XSSFWorkbook workbook) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            fileOut.flush();
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new OutputException(outputFile.getName(), e);
        }
    }
}
