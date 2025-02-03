package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Experiment;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.output.formats.*;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;
import java.util.Set;

public class OutputGenerator {

    private final boolean GUI;
    private final OutputStyle outputStyle;
    private final String outputFileName;
    private final boolean writeToDifferentFile;
    private final File inputFile;
    private final LoggerHelper LOGGER;
    private final String emptyInputSheetName;
    private final short numReplicates;
    private final Set<Condition> conditions;
    private final Set<Strain> strains;
    private final Set<Short> days;
    private final short numDays;
    private final boolean includeBaselineColumn;

    public OutputGenerator(Config config, LoggerHelper logger) {
        LOGGER = logger;
        switch (config.outputType()) {
            case PRISM:
                outputStyle = new PrismOutputStyle(config.sortOption());
                break;
            case STATISTICAL_TESTS:
                outputStyle = new StatisticalTestsOutputStyle(config.sortOption(), config.numberOfReplicates());
                break;
            case RAW:
                outputStyle = new RawOutputStyle(config.sortOption());
                break;
            case BOTH:
                outputStyle = new BothOutputStyle(config.sortOption(), config.numberOfReplicates());
                break;
            default:
                // This is redundant because the compiler thinks that outputStyle is not initialized
                outputStyle = null;
                LOGGER.atError("Output type not recognized");
                System.exit(0);
        }
        if (config.writeToDifferentFile()) this.outputFileName = config.outputFilename();
        else this.outputFileName = config.inputFile().getPath();
        this.writeToDifferentFile = config.writeToDifferentFile();
        this.inputFile = config.inputFile();
        this.emptyInputSheetName = config.emptyInputSheetName();
        this.numReplicates = config.numberOfReplicates();
        this.GUI = config.GUI();
        this.conditions = config.conditions();
        this.strains = config.strains();
        this.days = config.days();
        this.numDays = config.numDays();
        this.includeBaselineColumn = config.includeBaselineColumn();
    }
    public void generateOutput(Experiment experiment) throws OutputException {
        XlsxOutputWriter writer = new XlsxOutputWriter(outputStyle, writeToDifferentFile, inputFile);
        try {
            writer.writeOutput(outputFileName, experiment);
        } catch (OutputException e) {
            if (GUI) throw e;
            else {
                LOGGER.atError("Error writing output: " + e.getMessage() + " -  Exiting...");
                System.out.println("Error writing output: " + e.getMessage() + " - Exiting...");
                System.exit(0);
            }
        }

    }

    public void generateEmptyInputSheet() throws OutputException {
        XlsxEmptyInputSheetWriter emptyInputSheetWriter;
        if (this.conditions.isEmpty() && this.strains.isEmpty())
            throw new NoStrainsOrConditionsException();
        else if (this.numDays == 0 && this.days.isEmpty())
            throw new NoDaysException();
        else if (this.numDays != 0 && this.days.isEmpty()) {
            LOGGER.atInfo("No days specified. Using number of days and leaving day numbers blank in empty input sheet.");
            emptyInputSheetWriter = new XlsxEmptyInputSheetWriter(emptyInputSheetName, numReplicates, LOGGER, conditions, strains, numDays, includeBaselineColumn);
        } else
            emptyInputSheetWriter = new XlsxEmptyInputSheetWriter(emptyInputSheetName, numReplicates, LOGGER, conditions, strains, days, includeBaselineColumn);
        emptyInputSheetWriter.writeEmptyInputSheet();
    }
}
