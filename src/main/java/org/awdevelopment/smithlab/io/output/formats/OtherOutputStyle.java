package org.awdevelopment.smithlab.io.output.formats;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
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
        generatePrimaryData(sheet, experiment);
        generateSecondaryData(sheet, experiment);
    }

    private void generateSecondaryDataHeaders(String[] uniqueTypes, XSSFSheet sheet) {
        int headerRowIndex = sheet.getLastRowNum() + 3;
        XSSFRow headerRow = sheet.createRow(headerRowIndex);
        XSSFRow subheaderRow = sheet.createRow(headerRowIndex + 1);
        for (int i = 0; i < uniqueTypes.length; i++) {
            XSSFCell cell = headerRow.createCell((i*3) + 1);
            cell.setCellValue("%"+uniqueTypes[i]);
            XSSFCell daysHeaderCell = subheaderRow.createCell((i*3) + 1);
            daysHeaderCell.setCellValue("#days");
            XSSFCell deadHeaderCell = subheaderRow.createCell((i*3) + 2);
            deadHeaderCell.setCellValue("dead");
        }
    }

    private void generateSecondaryData(XSSFSheet sheet, Experiment experiment) {
        String[] uniqueTypes = getUniqueTypes(sheet);
        generateSecondaryDataHeaders(uniqueTypes, sheet);
        short[] dayNumbers = experiment.getDayNumbers();
        int differenceNumber = 1;
        for (int dayNumberIndex = 0; dayNumberIndex < dayNumbers.length; dayNumberIndex++) {
            short dayNumber = dayNumbers[dayNumberIndex];
            XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            int averageNumber = 1;
            for (int columnIndex = 1; columnIndex < (uniqueTypes.length * 3) + 1; columnIndex += 3) {
                XSSFCell dayNumberCell = row.createCell(columnIndex);
                dayNumberCell.setCellValue(dayNumber);
                XSSFCell deadCell = row.createCell(columnIndex + 1);
                if (dayNumberIndex == 0) {
                    deadCell.setCellValue(0);
                } else {
                    deadCell.setCellFormula(getAverageNDifferenceK(sheet, averageNumber, differenceNumber));
                }
                averageNumber++;
            }
            differenceNumber++;
        }
    }

    private String getAverageNDifferenceK(XSSFSheet sheet, int n, int k) {
        int curRowNumber = 0;
        int averageNumber = 0;
        int differenceNumber = 0;
        if (k == 1) return "0";
        while (averageNumber < n) {
            curRowNumber++;
            if (sheet.getRow(curRowNumber).getCell(0).getStringCellValue().equals("Average")) averageNumber++;
            else continue;
            differenceNumber = 1;
            while (differenceNumber < k) {
                differenceNumber++;
            }
        }
        String cellOneAddress = getStringCellAddress(sheet.getRow(curRowNumber).getCell(differenceNumber));
        String cellTwoAddress = getStringCellAddress(sheet.getRow(curRowNumber).getCell(differenceNumber - 1));
        return  "MAX(" + cellTwoAddress + "-" + cellOneAddress +" , 0)";
    }

    private String[] getUniqueTypes(XSSFSheet sheet) {
        String[] uniqueTypes = new String[getNumUniqueTypes(sheet)];
        int uniqueTypeCounter = 0;
        for (int i = numberOfReplicates; i < sheet.getLastRowNum() + 1; i += numberOfReplicates + 1) {
            uniqueTypes[uniqueTypeCounter] = sheet.getRow(i).getCell(0).getStringCellValue();
            uniqueTypeCounter++;
        }
        return uniqueTypes;
    }

    private int getNumUniqueTypes(XSSFSheet sheet) {
        int numUniqueTypes = 0;
        for (int i = numberOfReplicates + 1; i < sheet.getLastRowNum() + 1; i += numberOfReplicates + 1) {
            numUniqueTypes++;
        }
        return numUniqueTypes;
    }

    private void generatePrimaryData(XSSFSheet sheet, Experiment experiment) {
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
