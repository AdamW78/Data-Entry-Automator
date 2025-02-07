package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.io.output.formats.OutputType;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;

public class OutputSheetsConfig extends AbstractConfig {

    private static final Mode MODE = Mode.GENERATE_OUTPUT_SHEETS;

    public OutputSheetsConfig(Arguments args, LoggerHelper logger) {
        super(logger);
        addConfigEntry(new ConfigEntry<>(ConfigOption.INPUT_FILE, args.getInputFile()));
        addConfigEntry(new ConfigEntry<>(ConfigOption.OUTPUT_FILE, args.getOutputFileName()));
        addConfigEntry(new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, args.getOutputType()));
        addConfigEntry(new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, args.writeToDifferentFile()));
        addConfigEntry(new ConfigEntry<>(ConfigOption.SORT_OPTION, args.getOutputSorting()));
        addConfigEntry(new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, args.getReplicateNumber()));
    }

    public OutputSheetsConfig(LoggerHelper logger) {
        super(logger);
        addConfigEntry(new ConfigEntry<>(ConfigOption.INPUT_FILE, ConfigDefault.INPUT_FILE));
        addConfigEntry(new ConfigEntry<>(ConfigOption.OUTPUT_FILE, ConfigDefault.OUTPUT_FILENAME));
        addConfigEntry(new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, ConfigDefault.OUTPUT_TYPE));
        addConfigEntry(new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, ConfigDefault.WRITE_TO_DIFFERENT_FILE));
        addConfigEntry(new ConfigEntry<>(ConfigOption.SORT_OPTION, ConfigDefault.SORT_OPTION));
        addConfigEntry(new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, ConfigDefault.NUMBER_OF_REPLICATES));
    }

    public File inputFile() { return get(ConfigOption.INPUT_FILE); }
    public String outputFilename() { return get(ConfigOption.OUTPUT_FILE); }
    public OutputType outputType() { return get(ConfigOption.OUTPUT_TYPE); }
    public boolean writeToDifferentFile() { return get(ConfigOption.WRITE_TO_DIFFERENT_FILE); }
    public byte numberOfReplicates() { return get(ConfigOption.NUMBER_OF_REPLICATES); }
    public SortOption sortOption() { return get(ConfigOption.SORT_OPTION); }
    public void setInputFile(File inputFile) { set(ConfigOption.INPUT_FILE, inputFile); }
    public void setOutputFilename(String outputFilename) { set(ConfigOption.OUTPUT_FILE, outputFilename); }
    public void setOutputType(OutputType outputType) { set(ConfigOption.OUTPUT_TYPE, outputType); }
    public void setWriteToDifferentFile(boolean writeToDifferentFile) { set(ConfigOption.WRITE_TO_DIFFERENT_FILE, writeToDifferentFile); }
    public void setNumberOfReplicates(byte numberOfReplicates) { set(ConfigOption.NUMBER_OF_REPLICATES, numberOfReplicates); }

    @Override
    public String toString() { return "Output Sheets Configuration"; }
    @Override
    public Mode mode() { return MODE; }
}
