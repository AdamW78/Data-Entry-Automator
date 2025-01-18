package io.input;

import data.Experiment;
import io.Headers;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public record XlsxInputReader(File xlsxFile) {

    private static final int INPUT_SHEET_INDEX = 0;

    public Headers readHeaders(XSSFSheet sheet) {
        return XlsxHeaderReader.readHeaders(sheet);
    }

    public Experiment readExperimentData() {
        XSSFSheet sheet = getWorkbook().getSheetAt(INPUT_SHEET_INDEX);
        Headers headers = readHeaders(sheet);

    }

    private XSSFWorkbook getWorkbook() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
            return workbook;
        } catch (IOException e) {
            throw new RuntimeException("IOException: " + e.getMessage());
        } catch (InvalidFormatException e) {
            throw new RuntimeException("InvalidFormatException: " + e.getMessage());
        }
    }
}
