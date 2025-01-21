package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class Config {

    private final ConfigEntry<File> inputFile;
    private final ConfigEntry<Mode> mode;
    private final ConfigEntry<String> outputFile;
    private final ConfigEntry<OutputType> outputType;
    private final ConfigEntry<Boolean> writeToDifferentFile;
    private final ConfigEntry<SortOption> sortOption;
    private final ConfigEntry<Boolean> verbose;

    public Config() {
        this.inputFile = new ConfigEntry<>(ConfigOption.INPUT_FILE, null);
        this.mode = new ConfigEntry<>(ConfigOption.MODE, Mode.GENERATE_OUTPUT_SHEETS);
        this.outputFile = new ConfigEntry<>(ConfigOption.OUTPUT_FILE, null);
        this.outputType = new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, OutputType.BOTH);
        this.writeToDifferentFile = new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, null);
        this.verbose = new ConfigEntry<>(ConfigOption.VERBOSE, false);
        this.sortOption = new ConfigEntry<>(ConfigOption.PRISM_OUTPUT_SORTING, SortOption.SAMPLE_NUMBER);
    }

    public Config(Arguments arguments) {
        this.inputFile = new ConfigEntry<>(ConfigOption.INPUT_FILE, arguments.getInputFile());
        this.mode = new ConfigEntry<>(ConfigOption.MODE, arguments.getMode());
        this.outputFile = new ConfigEntry<>(ConfigOption.OUTPUT_FILE, arguments.getOutputFileName());
        this.outputType = new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, arguments.getOutputType());
        this.writeToDifferentFile = new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, arguments.writeToDifferentFile());
        this.verbose = new ConfigEntry<>(ConfigOption.VERBOSE, arguments.isVerbose());
        this.sortOption = new ConfigEntry<>(ConfigOption.PRISM_OUTPUT_SORTING, arguments.getPrismOutputSorting());
    }

    public File inputFile() {
        return inputFile.value();
    }

    public Mode mode() {
        return mode.value();
    }

    public String outputFile() {
        return outputFile.value();
    }

    public OutputType outputType() {
        return outputType.value();
    }

    public boolean writeToDifferentFile() {
        return writeToDifferentFile.value();
    }

    public SortOption sortOption() {
        return sortOption.value();
    }

    public boolean verbose() {
        return verbose.value();
    }

}
