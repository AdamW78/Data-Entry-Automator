package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.ConfigDefaults;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
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
        // these are not used in this constructor
        emptyInputSheetName = null;
        includeBaselineColumn = false;
        emptyExperiment = null;
    }

    public OutputGenerator(EmptyInputSheetConfig config) throws NoDaysException, NoStrainsOrConditionsException {
        LOGGER = config.LOGGER();
        outputFileName = ConfigDefaults.EMPTY_INPUT_SHEET_FILENAME;
        emptyInputSheetName = config.emptyInputSheetName();
        includeBaselineColumn = config.includeBaselineColumn();
        emptyExperiment = generateEmptyExperiment(config);
        GUI = false;
        // these are not used in this constructor
        this.outputStyle = null;
        this.writeToDifferentFile = false;
        this.inputFile = null;
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

    private EmptyExperiment generateEmptyExperiment(EmptyInputSheetConfig config) throws NoDaysException {
        Byte[] days = config.days().toArray(new Byte[0]);
        byte[] daysArray = new byte[days.length];
        for (int i = 0; i < days.length; i++) { daysArray[i] = days[i];}
        if (config.usingNumConditions() && config.usingNumStrains()) {
            return new EmptyExperiment(config.numStrains(), config.numConditions(), config.numReplicates(), daysArray, config.numDays());
        } else if (config.usingNumConditions()) {
            return new EmptyExperiment(config.strains(), config.numConditions(), config.numReplicates(), daysArray, config.numDays());
        } else if (config.usingNumStrains()) {
            return new EmptyExperiment(config.numStrains(), config.conditions(), config.numReplicates(), daysArray, config.numDays());
        } else {
            return new EmptyExperiment(config.strains(), config.conditions(), config.numReplicates(), daysArray, config.numDays());
        }
    }
}
