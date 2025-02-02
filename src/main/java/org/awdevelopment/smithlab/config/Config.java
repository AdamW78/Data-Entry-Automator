package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class Config {

    private final ConfigEntry<File> inputFile;
    private final ConfigEntry<Mode> mode;
    private final ConfigEntry<String> outputFilename;
    private final ConfigEntry<OutputType> outputType;
    private final ConfigEntry<Boolean> writeToDifferentFile;
    private final ConfigEntry<SortOption> sortOption;
    private final ConfigEntry<Short> numberOfReplicates;
    private final ConfigEntry<String> emptyInputSheetName;
    private final ConfigEntry<Boolean> GUI;

    public Config() {
        this.numberOfReplicates = new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, ConfigDefault.NUMBER_OF_REPLICATES);
        this.inputFile = new ConfigEntry<>(ConfigOption.INPUT_FILE, ConfigDefault.INPUT_FILE);
        this.mode = new ConfigEntry<>(ConfigOption.MODE, ConfigDefault.MODE);
        this.outputFilename = new ConfigEntry<>(ConfigOption.OUTPUT_FILE, ConfigDefault.OUTPUT_FILENAME);
        this.outputType = new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, ConfigDefault.OUTPUT_TYPE);
        this.writeToDifferentFile = new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, ConfigDefault.WRITE_TO_DIFFERENT_FILE);
        this.sortOption = new ConfigEntry<>(ConfigOption.SORT_OPTION, ConfigDefault.SORT_OPTION);
        this.emptyInputSheetName = new ConfigEntry<>(ConfigOption.EMPTY_INPUT_SHEET_NAME, ConfigDefault.EMPTY_INPUT_SHEET_NAME);
        this.GUI = new ConfigEntry<>(ConfigOption.GUI, Boolean.TRUE);
    }

    public Config(Arguments arguments) {
        this.inputFile = new ConfigEntry<>(ConfigOption.INPUT_FILE, arguments.getInputFile());
        this.mode = new ConfigEntry<>(ConfigOption.MODE, arguments.getMode());
        this.outputFilename = new ConfigEntry<>(ConfigOption.OUTPUT_FILE, arguments.getOutputFileName());
        this.outputType = new ConfigEntry<>(ConfigOption.OUTPUT_TYPE, arguments.getOutputType());
        this.writeToDifferentFile = new ConfigEntry<>(ConfigOption.WRITE_TO_DIFFERENT_FILE, arguments.writeToDifferentFile());
        this.sortOption = new ConfigEntry<>(ConfigOption.SORT_OPTION, arguments.getOutputSorting());
        this.numberOfReplicates = new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, arguments.getReplicateNumber());
        this.emptyInputSheetName = new ConfigEntry<>(ConfigOption.EMPTY_INPUT_SHEET_NAME, arguments.getEmptyInputSheetName());
        this.GUI = new ConfigEntry<>(ConfigOption.GUI, Boolean.FALSE);
    }

    public File inputFile() {
        return inputFile.value();
    }

    public Mode mode() {
        return mode.value();
    }

    public String outputFilename() {
        return outputFilename.value();
    }

    public OutputType outputType() {
        return outputType.value();
    }

    public boolean writeToDifferentFile() {
        return writeToDifferentFile.value();
    }

    public short numberOfReplicates() { return numberOfReplicates.value(); }

    public SortOption sortOption() { return sortOption.value(); }

    public String emptyInputSheetName() { return emptyInputSheetName.value(); }

    public boolean GUI() { return GUI.value(); }

    public void setOutputType(OutputType outputType) { this.outputType.setValue(outputType); }

    public void setInputFile(File inputFile) { this.inputFile.setValue(inputFile); }

    public void setMode(Mode mode) { this.mode.setValue(mode); }

    public void setOutputFilename(String outputFilename) { this.outputFilename.setValue(outputFilename); }

    public void setWriteToDifferentFile(boolean writeToDifferentFile) { this.writeToDifferentFile.setValue(writeToDifferentFile); }

    public void setNumberOfReplicates(short numberOfReplicates) { this.numberOfReplicates.setValue(numberOfReplicates); }

    public void setSortOption(SortOption sortOption) { this.sortOption.setValue(sortOption); }

    public void setEmptyInputSheetName(String emptyInputSheetName) { this.emptyInputSheetName.setValue(emptyInputSheetName); }
}
