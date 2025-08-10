package org.awdevelopment.smithlab.io.output;

import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.config.OutputSheetsConfig;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.data.experiment.EmptyExperiment;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.io.exceptions.OutputException;
import org.awdevelopment.smithlab.io.output.formats.*;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;
import java.util.*;

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
            case OASIS -> new StatisticalTestsOutputStyle(config.sortOption(), config.numberOfReplicates());
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
        outputFileName = config.emptyInputSheetName();
        emptyInputSheetName = config.emptyInputSheetName();
        includeBaselineColumn = config.includeBaselineColumn();
        emptyExperiment = generateEmptyExperiment(config);
        LOGGER.atDebug("EMPTY EXPERIMENT:\n" + emptyExperiment.readableExperiment());
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
        LOGGER.atDebug("DAYS: " + Arrays.toString(emptyExperiment.getDays()));
        LOGGER.atDebug("WRITING EMPTY INPUT SHEET WITH NAME: " + emptyInputSheetName);
        emptyInputSheetWriter = new XlsxEmptyInputSheetWriter(emptyInputSheetName, LOGGER, includeBaselineColumn, emptyExperiment);
        LOGGER.atDebug("SUCCESSFULLY CREATED EMPTY INPUT SHEET WRITER");
        emptyInputSheetWriter.writeEmptyInputSheet();
        LOGGER.atDebug("SUCCESSFULLY WROTE EMPTY INPUT SHEET TO FILE: " + emptyInputSheetName);
    }

    private EmptyExperiment generateEmptyExperiment(EmptyInputSheetConfig config) throws NoDaysException {
        Set<Condition> conditionsSet = new LinkedHashSet<>();
        Set<Strain> strainsSet = new LinkedHashSet<>();
        boolean usingNumConditions, usingNumStrains, usingNumDays;
        byte numConditions, numStrains, numReplicates, numDays;
        byte[] days;
        switch (config.sampleLabelingType()) {
            case SampleLabelingType.STRAIN -> {
                usingNumConditions = false;
                conditionsSet = Collections.emptySet();
                numConditions = -1; // -1 indicates no conditions
                usingNumStrains = config.usingNumStrains();
                if (usingNumStrains) {
                    numStrains = config.numStrains();
                    strainsSet = new HashSet<>();
                    for (int i = 0; i < numStrains; i++) { strainsSet.add(new Strain("Strain " + (i + 1))); }
                } else {
                    strainsSet = config.strains();
                    numStrains = (byte) config.strains().size();
                }
            }
            case SampleLabelingType.CONDITION -> {
                usingNumConditions = config.usingNumConditions();
                strainsSet = Collections.emptySet();
                numStrains = -1; // -1 indicates no strains
                usingNumStrains = false;
                if (usingNumConditions) {
                    numConditions = config.numConditions();
                    for (int i = 0; i < numConditions; i++) {
                        conditionsSet.add(new Condition("Condition " + (i + 1)));
                    }
                } else {
                    conditionsSet = config.conditions();
                    numConditions = (byte) config.conditions().size();
                }
            }
            case SampleLabelingType.CONDITION_AND_STRAIN -> {
                usingNumConditions = config.usingNumConditions();
                usingNumStrains = config.usingNumStrains();
                if (usingNumConditions) {
                    numConditions = config.numConditions();
                    for (int i = 0; i < numConditions; i++) {
                        conditionsSet.add(new Condition("Condition " + (i + 1)));
                    }
                } else {
                    conditionsSet = config.conditions();
                    numConditions = (byte) config.conditions().size();
                }
                if (usingNumStrains) {
                    numStrains = config.numStrains();
                    for (int i = 0; i < numStrains; i++) {
                        strainsSet.add(new Strain("Strain " + (i + 1)));
                    }
                } else {
                    strainsSet = config.strains();
                    numStrains = (byte) config.strains().size();
                }
            }
            default ->
                    throw new IllegalArgumentException("Invalid sample labeling type: " + config.sampleLabelingType());
        }
        if (config.usingNumDays()) {
            if (config.numDays() <= 0) throw new NoDaysException();
            numDays = config.numDays();
            days = new byte[numDays];
            for (int i = 0; i < numDays; i++) { days[i] = (byte) (i + 1); }
        } else {
            if (config.days().isEmpty()) throw new NoDaysException();
            Byte[] daysByteArray = new Byte[config.days().size()];
            Iterator<Byte> iterator = config.days().iterator();
            for (int i = 0; i < config.days().size(); i++) {
                daysByteArray[i] = iterator.next();
            }
            days = new byte[daysByteArray.length];
            for (int i = 0; i < daysByteArray.length; i++) {
                days[i] = daysByteArray[i];
            }
            numDays = (byte) days.length;
        }
        numReplicates = config.numReplicates();
        return new EmptyExperiment(conditionsSet, strainsSet, numReplicates, numConditions, numStrains,
                usingNumConditions, usingNumStrains, days, numDays, config.usingNumDays());
    }
}
