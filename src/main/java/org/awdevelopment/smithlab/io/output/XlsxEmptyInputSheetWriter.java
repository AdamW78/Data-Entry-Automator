package org.awdevelopment.smithlab.io.output;

import org.apache.poi.ss.formula.functions.Days;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.FailedToCreateNewWorkbookException;
import org.awdevelopment.smithlab.io.exceptions.WriteWorkbookToFileException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class XlsxEmptyInputSheetWriter {

    private final String emptyInputSheetName;
    private final short numReplicates;
    private final LoggerHelper LOGGER;
    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final Set<Short> days;
    private final short numDays;

    private final boolean usingNumDays;
    private final boolean includeBaselineColumn;

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, short numReplicates, LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, Set<Short> days, boolean includeBaselineColumn) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.numReplicates = numReplicates;
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = days;
        this.numDays = (short) days.size();
        this.usingNumDays = false;
        this.includeBaselineColumn = includeBaselineColumn;
    }

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, short numReplicates, LoggerHelper logger, Set<Condition> conditions, Set<Strain> strains, short numDays, boolean includeBaselineColumn) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.numReplicates = numReplicates;
        this.LOGGER = logger;
        this.conditions = conditions;
        this.strains = strains;
        this.days = new HashSet<>();
        this.numDays = numDays;
        this.usingNumDays = true;
        this.includeBaselineColumn = includeBaselineColumn;
    }

    public void writeEmptyInputSheet() throws FailedToCreateNewWorkbookException, WriteWorkbookToFileException {
        LOGGER.atInfo("Writing empty input sheet \"" + emptyInputSheetName + "\"...");
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XlsxEmptyInputSheetHeaderWriter headerWriter;
            if (usingNumDays) headerWriter = new XlsxEmptyInputSheetHeaderWriter(LOGGER, conditions, strains, numDays, includeBaselineColumn);
            else headerWriter = new XlsxEmptyInputSheetHeaderWriter(LOGGER, conditions, strains, days, includeBaselineColumn);
            headerWriter.generateHeaders(workbook.createSheet());
            try {
                workbook.write(new FileOutputStream(emptyInputSheetName));
            } catch (IOException e) {
                LOGGER.atError("Failed to write empty input sheet \"" + emptyInputSheetName + "\"");
                throw new WriteWorkbookToFileException(emptyInputSheetName, e);
            }
        } catch (WriteWorkbookToFileException e) {
            throw e;
        } catch (IOException e) {
            throw new FailedToCreateNewWorkbookException();
        }
    }
}
