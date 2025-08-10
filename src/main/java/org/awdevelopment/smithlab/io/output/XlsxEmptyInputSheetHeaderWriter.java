package org.awdevelopment.smithlab.io.output;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.util.Iterator;
import java.util.Set;

public class XlsxEmptyInputSheetHeaderWriter {

    private final LoggerHelper LOGGER;
    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final byte[] days;
    private final boolean usingNumDays;
    private final boolean includeBaselineColumn;
    private final byte numDays;
    private final byte numReplicates;

    public XlsxEmptyInputSheetHeaderWriter(LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, byte[] days, boolean includeBaselineColumn, byte numReplicates) {
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = days;
        this.numDays = (byte) days.length;
        this.usingNumDays = false;
        this.includeBaselineColumn = includeBaselineColumn;
        this.numReplicates = numReplicates;
    }

    public XlsxEmptyInputSheetHeaderWriter(LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, byte numDays, boolean includeBaselineColumn, byte numReplicates) {
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = null;
        this.numDays = numDays;
        this.usingNumDays = true;
        this.includeBaselineColumn = includeBaselineColumn;
        this.numReplicates = numReplicates;
    }

    public void generateHeaders(XSSFSheet emptyInputSheet) throws NoStrainsOrConditionsException {
        XSSFRow headerRow = emptyInputSheet.createRow(0);
        XSSFRow subHeaderRow = emptyInputSheet.createRow(1);
        LOGGER.atInfo("Writing empty input sheet headers...");
        byte lastUsedColumn = generateStrainConditionLabels(headerRow);
        headerRow.createCell(++lastUsedColumn).setCellValue("Sample Number");
        if (includeBaselineColumn) headerRow.createCell(++lastUsedColumn).setCellValue("Baseline");
        generateDayHeaders(headerRow, subHeaderRow, lastUsedColumn);

    }

    private void generateDayHeaders(XSSFRow headerRow, XSSFRow subHeaderRow, byte lastUsedColumn) {
        if (usingNumDays) {
            for (int i = 0; i < numDays; i++) {
                XSSFCell dayHeaderCell = headerRow.createCell(++lastUsedColumn);
                dayHeaderCell.setCellValue(i + 1);
                lastUsedColumn = createSingleDayHeaderMerge(headerRow, subHeaderRow, lastUsedColumn, dayHeaderCell);
            }
        } else {
            assert days != null;
            for (byte day : days) {
                XSSFCell dayHeaderCell = headerRow.createCell(++lastUsedColumn);
                dayHeaderCell.setCellValue(day);
                lastUsedColumn = createSingleDayHeaderMerge(headerRow, subHeaderRow, lastUsedColumn, dayHeaderCell);
            }
        }
    }

    private byte createSingleDayHeaderMerge(XSSFRow headerRow, XSSFRow subHeaderRow, byte lastUsedColumn, XSSFCell dayHeaderCell) {
        CellStyle dayHeaderCellStyle = headerRow.getSheet().getWorkbook().createCellStyle();
        dayHeaderCellStyle.setDataFormat(headerRow.getSheet().getWorkbook().createDataFormat().getFormat("\\D\\a\\y 0"));
        headerRow.createCell(++lastUsedColumn);
        headerRow.createCell(++lastUsedColumn);
        CellRangeAddress mergeRange = new CellRangeAddress(0, 0, lastUsedColumn - 2, lastUsedColumn);
        headerRow.getSheet().addMergedRegion(mergeRange);
        createDayHeader(subHeaderRow, lastUsedColumn, dayHeaderCell, dayHeaderCellStyle);
        return lastUsedColumn;
    }

    private void createDayHeader(XSSFRow subHeaderRow, byte lastUsedColumn, XSSFCell dayHeaderCell, CellStyle dayHeaderCellStyle) {
        dayHeaderCell.setCellType(CellType.NUMERIC);
        dayHeaderCell.setCellStyle(dayHeaderCellStyle);
        XSSFCell subHeaderCell = subHeaderRow.createCell(lastUsedColumn - 2);
        subHeaderCell.setCellValue("Colonies");
        subHeaderCell = subHeaderRow.createCell(lastUsedColumn - 1);
        subHeaderCell.setCellValue("Dilution");
        subHeaderCell = subHeaderRow.createCell(lastUsedColumn);
        subHeaderCell.setCellValue("% of Initial Value");
    }

    private byte generateStrainConditionLabels(XSSFRow headerRow) throws NoStrainsOrConditionsException {
        if (conditions.isEmpty() && strains.isEmpty()) {
            LOGGER.atError("No conditions or strains provided");
            throw new NoStrainsOrConditionsException();
        } else if (conditions.isEmpty()) {
            LOGGER.atInfo("No conditions provided - just using strains");
            headerRow.createCell(0).setCellValue("Strain");
            generateStrainLabels(headerRow, 0);
            return 0;
        } else if (strains.isEmpty()) {
            LOGGER.atInfo("No strains provided - just using conditions");
            headerRow.createCell(0).setCellValue("Condition");
            generateConditionLabels(headerRow, false);
            return 0;

        } else {
            LOGGER.atInfo("Conditions and strains provided");
            headerRow.createCell(0).setCellValue("Condition");
            headerRow.createCell(1).setCellValue("Strain");
            generateConditionLabels(headerRow, true);
            generateStrainLabels(headerRow, 1);
            return 1;
        }
    }

    private void generateConditionLabels(XSSFRow headerRow, boolean usingStrains) {
        int curRowNum = 2;
        int mergedAreaHeight;
        if (usingStrains) {
            mergedAreaHeight = numReplicates * strains.size();
        } else {
            mergedAreaHeight = numReplicates;
        }
        for (Condition condition : conditions) {
            LOGGER.atInfo("Generating condition: " + condition);
            for (int i = 0; i < mergedAreaHeight; i++) {
                headerRow.getSheet().createRow(curRowNum + i).createCell(0);
            }
            XSSFCell conditionLabelCell = headerRow.getSheet().getRow(curRowNum).getCell(0);
            conditionLabelCell.setCellValue(condition.getName());
            // Create a merged cell region that spans the number of replicates for the condition
            CellRangeAddress conditionLabelCellAddress = new CellRangeAddress(curRowNum, curRowNum + (mergedAreaHeight - 1), 0, 0);
            headerRow.getSheet().addMergedRegion(conditionLabelCellAddress);
            curRowNum += mergedAreaHeight;
            CellStyle conditionLabelStyle = headerRow.getSheet().getWorkbook().createCellStyle();
            conditionLabelStyle.setWrapText(true);
            conditionLabelCell.setCellStyle(conditionLabelStyle);

        }
    }

    private void generateStrainLabels(XSSFRow headerRow, int columnIndex) {
        int curRowNum = 2;
        if (columnIndex == 1) {
            for (int i = 0; i < conditions.size(); i++) {
                generateOneSetOfStrainLabels(headerRow, columnIndex, curRowNum, numReplicates);
                curRowNum += numReplicates * strains.size();
            }
        } else {
            generateOneSetOfStrainLabels(headerRow, columnIndex, curRowNum, numReplicates);
        }

    }

    private void generateOneSetOfStrainLabels(XSSFRow headerRow, int columnIndex, int curRowNum, int numReplicates) {
        for (Strain strain : strains) {
            // Create a merged cell region that spans the number of replicates for the strain
            CellRangeAddress strainLabelCellAddress = new CellRangeAddress(curRowNum, curRowNum + (numReplicates - 1), columnIndex, columnIndex);
            headerRow.getSheet().addMergedRegion(strainLabelCellAddress);
            for (int i = 0; i < numReplicates; i++) {
                if (headerRow.getSheet().getRow(curRowNum + i) == null) {
                    headerRow.getSheet().createRow(curRowNum + i);
                }
            }
            XSSFCell strainLabelCell = headerRow.getSheet().getRow(curRowNum).createCell(columnIndex);
            curRowNum += numReplicates;
            strainLabelCell.setCellValue(strain.getName());
            CellStyle strainLabelStyle = headerRow.getSheet().getWorkbook().createCellStyle();
            strainLabelStyle.setWrapText(true);
            strainLabelCell.setCellStyle(strainLabelStyle);
        }
    }
}
