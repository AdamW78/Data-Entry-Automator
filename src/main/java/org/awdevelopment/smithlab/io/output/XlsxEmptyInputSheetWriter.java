package org.awdevelopment.smithlab.io.output;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.data.experiment.EmptyExperiment;
import org.awdevelopment.smithlab.io.exceptions.FailedToCreateNewWorkbookException;
import org.awdevelopment.smithlab.io.exceptions.WriteWorkbookToFileException;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class XlsxEmptyInputSheetWriter {

    private final String emptyInputSheetName;
    private final byte numReplicates;
    private final LoggerHelper LOGGER;
    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final byte[] days;
    private final byte numDays;

    private final boolean usingNumDays;
    private final boolean includeBaselineColumn;

    public XlsxEmptyInputSheetWriter(String emptyInputSheetName, LoggerHelper logger, boolean includeBaselineColumn, EmptyExperiment emptyExperiment) {
        this.emptyInputSheetName = emptyInputSheetName;
        this.numReplicates = emptyExperiment.getNumReplicates();
        this.LOGGER = logger;
        this.conditions = emptyExperiment.conditions();
        this.strains = emptyExperiment.strains();
        this.days = emptyExperiment.getDays();
        this.numDays = emptyExperiment.getNumDays();
        this.usingNumDays = emptyExperiment.usingNumDays();
        this.includeBaselineColumn = includeBaselineColumn;
    }

    public void writeEmptyInputSheet() throws FailedToCreateNewWorkbookException, WriteWorkbookToFileException {
        LOGGER.atInfo("Writing empty input sheet \"" + emptyInputSheetName + "\"...");
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XlsxEmptyInputSheetHeaderWriter headerWriter;
            if (usingNumDays) headerWriter = new XlsxEmptyInputSheetHeaderWriter(LOGGER, conditions, strains, numDays, includeBaselineColumn, numReplicates);
            else headerWriter = new XlsxEmptyInputSheetHeaderWriter(LOGGER, conditions, strains, days, includeBaselineColumn, numReplicates);
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
