package org.awdevelopment.smithlab.io.output.formats;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.data.Sample;

public class PrismOutputStyle extends OutputStyle {

    public PrismOutputStyle(SortOption sortOption) {
        super(OutputType.PRISM, sortOption);
    }


    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        generateLabels(sheets, experiment);
        generatePrismOutput(sheets[sheets.length - 1], experiment);
    }

    private void generatePrismOutput(XSSFSheet sheet, Experiment experiment) {
        generateSampleHeaders(sheet, experiment);
        generateDayNumberHeaders(sheet, experiment.getDayNumbers());
        generateData(sheet, experiment);
    }

    private void generateData(XSSFSheet sheet, Experiment experiment) {
        for (int i = 0; i < experiment.getDayNumbers().length; i++) {
            short dayNumber = experiment.getDayNumbers()[i];
            XSSFRow row = sheet.getRow(i + 1);
            for (int j = 0; j < experiment.getSamples().size(); j++) {
                Sample sample = switch (sortOption) {
                    case NONE -> (Sample) experiment.getSamples().toArray()[j];
                    case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(j);
                    case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(j);
                    case SAMPLE_NUMBER -> experiment.getSampleNumber(j + 1);
                };
                XSSFCell cell = row.createCell(j + 1);
                String sheetName = sample.getBaselineCell().getSheet().getSheetName();
                String baselineCellAddress = getStringCellAddress(sample.getBaselineCell());
                String timepointCellAddress = getStringCellAddress(sample.getTimepointByDay(dayNumber).originalCell());
                String dilutionCellAddress = getStringCellAddress(sample.getTimepointByDay(dayNumber).dilutionCell());
                String cellFormula = "'" + sheetName + "'!" + timepointCellAddress + "*" + "'" + sheetName + "'!" + dilutionCellAddress + "/'" + sheetName + "'!" + baselineCellAddress;
                cell.setCellFormula(cellFormula);
                cell.setCellStyle(sheet.getWorkbook().createCellStyle());
                cell.getCellStyle().setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("0.000"));
            }
        }
    }

    private void generateSampleHeaders(XSSFSheet sheet, Experiment experiment) {
        for (int i = 0; i < experiment.getSamples().size(); i++) {
            Sample sample = switch (sortOption) {
                case NONE -> (Sample) experiment.getSamples().toArray()[i];
                case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(i);
                case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(i);
                case SAMPLE_NUMBER -> experiment.getSampleNumber(i + 1);
            };
            XSSFCell cell = sheet.getRow(0).createCell(i + 1);
            cell.setCellValue(sample.getOutputName());
        }
    }

    private void generateDayNumberHeaders(XSSFSheet sheet, short[] dayNumbers) {
        for (int i = 0; i < dayNumbers.length; i++) {
            XSSFRow row = sheet.createRow(i + 1);
            XSSFCell cell = row.createCell(0);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(dayNumbers[i]);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("\"Day \"0"));
            cell.setCellStyle(cellStyle);
        }
    }
}
