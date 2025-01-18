package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.formats.OutputStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxOutputWriter {

    private final OutputStyle outputStyle;

    public XlsxOutputWriter(OutputStyle outputStyle) {
        this.outputStyle = outputStyle;
    }

    private int getNumberOfSheets() {
        return switch (outputStyle.getOutputType()) {
            case PRISM, RAW, OTHER -> 1;
            case BOTH -> 2;
        };
    }

    public void writeOutput(String outputFileName, Experiment experiment) throws OutputException {
        File outputFile = new File(outputFileName);
        try (XSSFWorkbook workbook = new XSSFWorkbook(outputFile)) {
            XSSFSheet[] sheets = new XSSFSheet[getNumberOfSheets()];
            for (int i = 0; i < sheets.length; i++) {
                sheets[i] = workbook.createSheet();
            }
            outputStyle.generateOutputSheets(sheets, experiment);
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
