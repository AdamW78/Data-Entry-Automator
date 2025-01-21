package org.awdevelopment.smithlab.io.output.formats;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.data.Sample;
import org.awdevelopment.smithlab.io.exceptions.NoStrainOrConditionException;

public class PrismOutputStyle extends OutputStyle {

    private final SortOption prismOutputSorting;

    public PrismOutputStyle(SortOption prismOutputSorting) {
        super(OutputType.PRISM);
        this.prismOutputSorting = prismOutputSorting;
    }


    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        XSSFSheet sheet = sheets[sheets.length - 1];
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        if (!experiment.getStrains().isEmpty() && !experiment.getConditions().isEmpty()) {
            cell.setCellValue("Strain + Condition");
        } else if (!experiment.getStrains().isEmpty()) {
            cell.setCellValue("Strain");
        } else if (!experiment.getConditions().isEmpty()) {
            cell.setCellValue("Condition");
        } else {
            throw new NoStrainOrConditionException();
        }
        generatePrismOutput(sheet, experiment);
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
                Sample sample = switch (prismOutputSorting) {
                    case NONE -> (Sample) experiment.getSamples().toArray()[i];
                    case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(i);
                    case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(i);
                    case SAMPLE_NUMBER -> experiment.getSampleNumber(i + 1);
                };
                XSSFCell cell = row.createCell(j + 1);
                String sheetName = sample.getBaselineCell().getSheet().getSheetName();
                String baselineCellName = getStringCellName(sample.getBaselineCell().getRowIndex(), sample.getBaselineCell().getColumnIndex());
                String timepointCellName = getStringCellName(sample.getTimepointByDay(dayNumber).originalCell().getRowIndex(), sample.getTimepointByDay(dayNumber).originalCell().getColumnIndex());
                String dilutionCellName = getStringCellName(sample.getTimepointByDay(dayNumber).originalCell().getRow().getCell(sample.getTimepointByDay(dayNumber).originalCell().getColumnIndex()).getRowIndex(),
                        sample.getTimepointByDay(dayNumber).originalCell().getRow().getCell(sample.getTimepointByDay(dayNumber).originalCell().getColumnIndex()).getColumnIndex() + 1);
                String cellFormula = "'" + sheetName + "'!" + timepointCellName + "*" + "'" + sheetName + "'!" + dilutionCellName + "/'" + sheetName + "'!" + baselineCellName;
                cell.setCellFormula(cellFormula);
                cell.setCellStyle(sheet.getWorkbook().createCellStyle());
                cell.getCellStyle().setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("0.000"));
            }
        }
    }

    private String getStringCellName(int row, int column) {
        String cellName;
        if (column < 26) {
            cellName = String.valueOf((char) (column + 65));
        } else {
            cellName = (char) (column / 26 + 64) + String.valueOf((char) (column % 26 + 65));
        }
        cellName += row + 1;
        return cellName;
    }

    private void generateSampleHeaders(XSSFSheet sheet, Experiment experiment) {
        for (int i = 0; i < experiment.getSamples().size(); i++) {
            Sample sample = switch (prismOutputSorting) {
                case NONE -> (Sample) experiment.getSamples().toArray()[i];
                case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(i);
                case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(i);
                case SAMPLE_NUMBER -> experiment.getSampleNumber(i + 1);
            };
            XSSFCell cell = sheet.getRow(0).createCell(i + 1);
            cell.setCellValue(sample.getPrismName());
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
