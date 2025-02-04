package org.awdevelopment.smithlab.io.output;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.io.exceptions.FailedToCopySheetsException;
import org.awdevelopment.smithlab.io.exceptions.FailedToCreateNewWorkbookException;
import org.awdevelopment.smithlab.io.exceptions.WriteWorkbookToFileException;
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
            case PRISM, RAW, STATISTICAL_TESTS -> 1;
            case BOTH -> 2;
        };
    }
    public void writeOutput(String outputFileName, Experiment experiment) throws OutputException {
        File outputFile = new File(outputFileName);
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            if (writeToDifferentFile && outputFile.exists()) copySheets(outputFile, workbook);
            copySheets(inputFile, workbook);
            for (int i = 0; i < getNumberOfSheets(); i++) workbook.createSheet();
            XSSFSheet[] sheets = new XSSFSheet[workbook.getNumberOfSheets()];
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) sheets[i] = workbook.getSheetAt(i);
            renameGeneratedOutputSheets(sheets);
            outputStyle.generateOutputSheets(sheets, experiment);
            try { writeWorkbookToFile(outputFile, workbook); }
            catch (FailedToCopySheetsException e) { throw e; }
            catch (IOException e) { throw new WriteWorkbookToFileException(outputFile.getPath(), e); }
        }
        catch (FailedToCopySheetsException | WriteWorkbookToFileException e) { throw e; }
        catch (IOException e) { throw new FailedToCreateNewWorkbookException(); }
    }

    private void renameGeneratedOutputSheets(XSSFSheet[] sheets) {
        int lastSheetIndex = sheets.length - 1;
        switch (outputStyle.getOutputType()) {
            case PRISM -> setSheetName(sheets[lastSheetIndex].getWorkbook(), sheets[lastSheetIndex], "PRISM");
            case RAW -> setSheetName(sheets[lastSheetIndex].getWorkbook(), sheets[lastSheetIndex], "RAW");
            case BOTH -> {
                setSheetName(sheets[lastSheetIndex - 1].getWorkbook(), sheets[lastSheetIndex - 1], "PRISM");
                setSheetName(sheets[lastSheetIndex].getWorkbook(), sheets[lastSheetIndex], "OTHER");
            }
            case STATISTICAL_TESTS -> setSheetName(sheets[lastSheetIndex].getWorkbook(), sheets[lastSheetIndex], "OTHER");
        }
    }

    private void copySheets(File inputFile, XSSFWorkbook outputWorkbook) throws OutputException {
        try (XSSFWorkbook inputWorkbook = new XSSFWorkbook(inputFile)) {
            for (int i = 0; i < inputWorkbook.getNumberOfSheets(); i++) {
                XSSFSheet inputSheet = inputWorkbook.getSheetAt(i);
                XSSFSheet outputSheet = outputWorkbook.createSheet();
                copySheet(inputSheet, outputSheet);
                setSheetName(outputWorkbook, outputSheet, inputSheet.getSheetName());
            }
        } catch (IOException e) {
            throw new FailedToCopySheetsException(inputFile, e);
        } catch (InvalidFormatException e) {
            throw new FailedToCopySheetsException(inputFile, e);
        }
    }


    private static void setSheetName(XSSFWorkbook workbook, XSSFSheet sheet, String name) {
        int sheetIndex = workbook.getSheetIndex(sheet);
        int k = 0;
        while (true) {
            try {
                if (k == 0) {
                    workbook.setSheetName(sheetIndex, name);
                } else {
                    workbook.setSheetName(sheetIndex, name + " (" + k + ")");
                }
                break;
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("The workbook already contains a sheet of this name")) {
                    k++;
                } else {
                    throw e;
                }
            }
        }
    }

    private static void copySheet(XSSFSheet source, XSSFSheet destination) {
        XSSFRangeCopier xssfRangeCopier = new XSSFRangeCopier(source, destination);
        int lastRow = source.getLastRowNum();
        int lastCol = 0;
        if (lastRow == -1) {
            return;
        }
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
            throw new WriteWorkbookToFileException(outputFile.getPath(), e);
        }
    }
}
