package org.awdevelopment.smithlab.io.output.formats;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.data.Sample;

public class OtherOutputStyle extends OutputStyle {

    private final short numberOfReplicates;

    public OtherOutputStyle(SortOption sortOption, short numberOfReplicates) {
        super(OutputType.OTHER, sortOption);
        this.numberOfReplicates = numberOfReplicates;
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        generateLabels(sheets, experiment);
        generateOtherOutput(sheets[sheets.length - 1], experiment);
    }

    private void generateOtherOutput(XSSFSheet sheet, Experiment experiment) {
        generateSampleHeaders(sheet, experiment);
        generateDayNumberHeaders(sheet, experiment.getDayNumbers());
        generateData(sheet, experiment);
    }

    private void generateData(XSSFSheet sheet, Experiment experiment) {
        int sampleCounter = 0;
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            for (int j = 0; j < experiment.getDayNumbers().length; j++) {
                XSSFCell cell = sheet.getRow(i).createCell(j + 1);
                if (i % (numberOfReplicates + 1) == 0) {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    String firstReplicateAddress = getStringCellAddress(sheet.getRow(rowIndex - numberOfReplicates).getCell(columnIndex));
                    String lastReplicateAddress = getStringCellAddress(sheet.getRow(rowIndex - 1).getCell(columnIndex));
                    cell.setCellFormula("AVERAGE(" + firstReplicateAddress + ":" + lastReplicateAddress + ")");
                } else {
                    Sample sample = switch (sortOption) {
                        case NONE -> (Sample) experiment.getSamples().toArray()[sampleCounter];
                        case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(sampleCounter);
                        case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(sampleCounter);
                        case SAMPLE_NUMBER -> experiment.getSampleNumber(sampleCounter + 1);
                    };
                    String sheetName = sample.getBaselineCell().getSheet().getSheetName();
                    String timepointCellAddress = getStringCellAddress(sample.getTimepointByDay(experiment.getDayNumbers()[j]).originalCell());
                    String dilutionCellAddress = getStringCellAddress(sample.getTimepointByDay(experiment.getDayNumbers()[j]).dilutionCell());
                    String cellFormula = "'" + sheetName + "'!" + timepointCellAddress + "*" + "'" + sheetName + "'!" + dilutionCellAddress;
                    cell.setCellFormula(cellFormula);
                }
                cell.setCellStyle(sheet.getWorkbook().createCellStyle());
                cell.getCellStyle().setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("0.000"));
            }
            if (i % (numberOfReplicates + 1) != 0) sampleCounter++;
        }
    }

    private void generateDayNumberHeaders(XSSFSheet sheet, short[] dayNumbers) {
        for (int i = 0; i < dayNumbers.length; i++) {
            XSSFCell cell = sheet.getRow(0).createCell(i + 1);
            cell.setCellValue(dayNumbers[i]);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("\"Day \"0"));
            cell.setCellStyle(cellStyle);
        }
    }

    private void generateSampleHeaders(XSSFSheet sheet, Experiment experiment) {
        int numRows = experiment.getSamples().size() + (experiment.getSamples().size() / numberOfReplicates) + 1;
        int sampleCounter = 0;
        for (int i = 1; i < numRows; i++) {
            XSSFCell cell = sheet.createRow(i).createCell(0);
            if (i % (numberOfReplicates + 1) == 0) {
                cell.setCellValue("Average");
            } else {
                Sample sample = switch (sortOption) {
                    case NONE -> (Sample) experiment.getSamples().toArray()[sampleCounter];
                    case ALPHABETICAL -> experiment.getSampleByAlphabeticalOrder(sampleCounter);
                    case REVERSE_ALPHABETICAL -> experiment.getSampleByReverseAlphabeticalOrder(sampleCounter);
                    case SAMPLE_NUMBER -> experiment.getSampleNumber(sampleCounter + 1);
                };
                cell.setCellValue(sample.getOutputName());
                sampleCounter++;
            }
        }
    }
}
