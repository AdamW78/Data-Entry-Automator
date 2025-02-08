package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.config.ConfigDefaults;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.data.experiment.EmptyExperiment;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.output.formats.*;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;

public class OutputGenerator {

    private final boolean GUI;
    private final OutputStyle outputStyle;
    private final String outputFileName;
    private final boolean writeToDifferentFile;
    private final File inputFile;
    private final LoggerHelper LOGGER;
    private final String emptyInputSheetName;
    private final boolean includeBaselineColumn;
    private final EmptyExperiment emptyExperiment;

    public OutputGenerator(Config config, LoggerHelper logger) throws NoDaysException {
        LOGGER = logger;
        switch (config.outputType()) {
            case PRISM:
                outputStyle = new PrismOutputStyle(config.sortOption());
                break;
            case STATISTICAL_TESTS:
                outputStyle = new StatisticalTestsOutputStyle(config.sortOption(), config.numReplicates());
                break;
            case RAW:
                outputStyle = new RawOutputStyle(config.sortOption());
                break;
            case BOTH:
                outputStyle = new BothOutputStyle(config.sortOption(), config.numReplicates());
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
        this.GUI = config.GUI();
        this.includeBaselineColumn = config.includeBaselineColumn();
        if (config.mode() == Mode.GENERATE_EMPTY_INPUT_SHEET) {
            Byte[] dayByteObjects = config.days().toArray(new Byte[0]);
            byte[] days = new byte[dayByteObjects.length];
            for (int i = 0; i < days.length; i++) { days[i] = dayByteObjects[i]; }
            this.emptyExperiment = new EmptyExperiment(config.strains(), config.conditions(), config.numReplicates(),
                    days, config.numDays());
        } else {
            this.emptyExperiment = null;
        }
    }

    public OutputGenerator(OutputSheetsConfig config) {
        LOGGER = config.LOGGER();
        outputStyle = switch (config.outputType()) {
            case PRISM -> new PrismOutputStyle(config.sortOption());
            case STATISTICAL_TESTS -> new StatisticalTestsOutputStyle(config.sortOption(), config.numberOfReplicates());
            case RAW -> new RawOutputStyle(config.sortOption());
            case BOTH -> new BothOutputStyle(config.sortOption(), config.numberOfReplicates());
        };
        if (config.writeToDifferentFile()) this.outputFileName = config.outputFilename();
        else this.outputFileName = config.inputFile().getPath();
        this.writeToDifferentFile = config.writeToDifferentFile();
        this.inputFile = config.inputFile();
        this.GUI = config.GUI();
    }

    public OutputGenerator(EmptyInputSheetConfig config) {
        LOGGER = config.LOGGER();
        outputFileName = ConfigDefaults.EMPTY_INPUT_SHEET_FILENAME;
        emptyInputSheetName = config.emptyInputSheetName();
        includeBaselineColumn = config.includeBaselineColumn();
        emptyExperiment = new EmptyExperiment(config.strains(), config.conditions(), config.numReplicates(),
                config.days(), config.numDays());
        GUI = false;
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
        if (this.emptyExperiment.hasNoStrainsOrConditions())
            throw new NoStrainsOrConditionsException();
        else if (this.emptyExperiment.usingNumDays()) {
            LOGGER.atInfo("No days specified. Using number of days and leaving day numbers blank in empty input sheet.");
        }
        emptyInputSheetWriter = new XlsxEmptyInputSheetWriter(emptyInputSheetName, LOGGER, includeBaselineColumn, emptyExperiment);
        emptyInputSheetWriter.writeEmptyInputSheet();
    }
}
