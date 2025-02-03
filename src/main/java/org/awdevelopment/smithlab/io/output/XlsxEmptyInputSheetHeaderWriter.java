package org.awdevelopment.smithlab.io.output;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.util.HashSet;
import java.util.Set;

public class XlsxEmptyInputSheetHeaderWriter {

    private final LoggerHelper LOGGER;
    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final Set<Short> days;
    private final boolean noDaysProvided;
    private final boolean includeBaselineColumn;
    private final short numDays;

    public XlsxEmptyInputSheetHeaderWriter(LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, Set<Short> days, boolean includeBaselineColumn) {
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = days;
        this.numDays = (short) days.size();
        this.noDaysProvided = days.isEmpty();
        this.includeBaselineColumn = includeBaselineColumn;
    }

    public XlsxEmptyInputSheetHeaderWriter(LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, short numDays, boolean includeBaselineColumn) {
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = new HashSet<>();
        this.numDays = numDays;
        this.noDaysProvided = true;
        this.includeBaselineColumn = includeBaselineColumn;
    }

    public void generateHeaders(XSSFSheet emptyInputSheet) throws NoStrainsOrConditionsException {
        XSSFRow headerRow = emptyInputSheet.createRow(0);
        XSSFRow subHeaderRow = emptyInputSheet.createRow(1);
        LOGGER.atInfo("Writing empty input sheet headers...");
        short lastUsedColumn = generateStrainConditionLabels(headerRow);
        headerRow.createCell(++lastUsedColumn).setCellValue("Sample Number");
        if (includeBaselineColumn) headerRow.createCell(++lastUsedColumn).setCellValue("Baseline");
        generateDayHeaders(headerRow, subHeaderRow, lastUsedColumn);

    }

    private void generateDayHeaders(XSSFRow headerRow, XSSFRow subHeaderRow, short lastUsedColumn) {
        if (noDaysProvided) {
            for (int i = 0; i < numDays; i++) {
                XSSFCell dayHeaderCell = headerRow.createCell(++lastUsedColumn);
                dayHeaderCell.setCellValue("0");
                CellStyle dayHeaderCellStyle = headerRow.getSheet().getWorkbook().createCellStyle();
                dayHeaderCellStyle.setDataFormat(headerRow.getSheet().getWorkbook().createDataFormat().getFormat("\\D\\a\\y 0"));
                headerRow.createCell(++lastUsedColumn);
                headerRow.createCell(++lastUsedColumn);
                CellRangeAddress mergeRange = new CellRangeAddress(0, 0, lastUsedColumn - 2, lastUsedColumn);
                headerRow.getSheet().addMergedRegion(mergeRange);
                XSSFCell subHeaderCell = subHeaderRow.createCell(lastUsedColumn - 2);
                subHeaderCell.setCellValue("Colonies");
                subHeaderCell = subHeaderRow.createCell(lastUsedColumn - 1);
                subHeaderCell.setCellValue("Dilution");
                subHeaderCell = subHeaderRow.createCell(lastUsedColumn);
                subHeaderCell.setCellValue("% of Initial Value");
            }
        }
    }

    private short generateStrainConditionLabels(XSSFRow headerRow) throws NoStrainsOrConditionsException {
        if (conditions.isEmpty() && strains.isEmpty()) {
            LOGGER.atError("No conditions or strains provided");
            throw new NoStrainsOrConditionsException();
        } else if (conditions.isEmpty()) {
            LOGGER.atInfo("No conditions provided - just using strains");
            headerRow.createCell(0).setCellValue("Strain");
            return 0;
        } else if (strains.isEmpty()) {
            LOGGER.atInfo("No strains provided - just using conditions");
            headerRow.createCell(0).setCellValue("Condition");
            return 0;
        } else {
            LOGGER.atInfo("Conditions and strains provided");
            headerRow.createCell(0).setCellValue("Condition");
            headerRow.createCell(1).setCellValue("Strain");
            return 1;
        }
    }
}
