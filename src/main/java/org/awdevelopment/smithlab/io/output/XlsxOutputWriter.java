package org.awdevelopment.smithlab.io.output;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.io.output.formats.OutputStyle;
import org.awdevelopment.smithlab.io.exceptions.OutputException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxOutputWriter {

    private final OutputStyle outputStyle;
    private final boolean writeToDifferentFile;
    private final File inputFile;

    public XlsxOutputWriter(OutputStyle outputStyle, boolean writeToDifferentFile, File inputFile) {
        this.outputStyle = outputStyle;
        this.inputFile = inputFile;
        this.writeToDifferentFile = writeToDifferentFile;
    }

    private int getNumberOfSheets() {
        return switch (outputStyle.getOutputType()) {
            case PRISM, RAW, OTHER -> 1;
            case BOTH -> 2;
        };
    }
    public void writeOutput(String outputFileName, Experiment experiment) throws OutputException {
        File outputFile = new File(outputFileName);
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            int existingSheets = getNumberOfSheets();
            try (XSSFWorkbook inputWorkbook = new XSSFWorkbook(inputFile)) {
                existingSheets += inputWorkbook.getNumberOfSheets();
                XSSFSheet[] sheets = new XSSFSheet[existingSheets];
                for (int i = 0; i < sheets.length; i++) {
                    sheets[i] = workbook.createSheet();
                }
                for (int i = 0; i < inputWorkbook.getNumberOfSheets(); i++) {
                    XSSFSheet inputSheet = inputWorkbook.getSheetAt(i);
                    XSSFSheet outputSheet = sheets[i];
                    copySheet(inputSheet, outputSheet);
                    workbook.setSheetName(i, inputWorkbook.getSheetName(i));
                }
                outputStyle.generateOutputSheets(sheets, experiment);
                writeWorkbookToFile(outputFile, workbook);
            } catch (IOException | InvalidFormatException e) {
                throw new OutputException(outputFileName, e);
            }
        } catch (IOException e) {
            throw new OutputException(outputFileName, e);
        }
    }

    private static void copySheet(XSSFSheet source, XSSFSheet destination) {
        XSSFRangeCopier xssfRangeCopier = new XSSFRangeCopier(source, destination);
        int lastRow = source.getLastRowNum();
        int lastCol = 0;
        for (int i = 0; i < lastRow; i++) {
            XSSFRow row = source.getRow(i);
            if (row != null) {
                if (row.getLastCellNum() > lastCol) {
                    lastCol = row.getLastCellNum();
                }
                destination.setDefaultRowHeight(source.getDefaultRowHeight());
            }
        }
        for (int j = 0; j < lastCol; j++) {
            destination.setColumnWidth(j, source.getColumnWidth(j));
        }
        CellRangeAddress cellAddresses = new CellRangeAddress(0, lastRow, 0, lastCol);
        xssfRangeCopier.copyRange(cellAddresses, cellAddresses, true, true);
    }

    private void writeWorkbookToFile(File outputFile, XSSFWorkbook workbook) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new OutputException(outputFile.getPath(), e);
        }
    }
}
