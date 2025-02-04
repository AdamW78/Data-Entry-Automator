package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;
import java.util.Set;

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
    private final ConfigEntry<Set<Condition>> conditions;
    private final ConfigEntry<Set<Strain>> strains;
    private final ConfigEntry<Set<Short>> days;
    private final ConfigEntry<Short> numDays;
    private final ConfigEntry<Boolean> includeBaselineColumn;

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
        this.conditions = new ConfigEntry<>(ConfigOption.CONDITIONS, ConfigDefault.CONDITIONS);
        this.strains = new ConfigEntry<>(ConfigOption.STRAINS, ConfigDefault.STRAINS);
        this.days = new ConfigEntry<>(ConfigOption.DAYS, ConfigDefault.DAYS);
        this.numDays = new ConfigEntry<>(ConfigOption.NUM_DAYS, ConfigDefault.NUM_DAYS);
        this.includeBaselineColumn = new ConfigEntry<>(ConfigOption.INCLUDE_BASELINE_COLUMN, ConfigDefault.INCLUDE_BASELINE_COLUMN);
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
        this.conditions = new ConfigEntry<>(ConfigOption.CONDITIONS, arguments.getConditions());
        this.strains = new ConfigEntry<>(ConfigOption.STRAINS, arguments.getStrains());
        this.days = new ConfigEntry<>(ConfigOption.DAYS, arguments.getDays());
        this.numDays = new ConfigEntry<>(ConfigOption.NUM_DAYS, arguments.getNumDays());
        this.includeBaselineColumn = new ConfigEntry<>(ConfigOption.INCLUDE_BASELINE_COLUMN, arguments.getIncludeBaselineColumn());
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

    public Set<Condition> conditions() { return conditions.value(); }

    public Set<Strain> strains() { return strains.value(); }

    public Set<Short> days() { return days.value(); }

    public short numDays() { return numDays.value(); }

    public boolean includeBaselineColumn() { return includeBaselineColumn.value(); }

    public void setOutputType(OutputType outputType) { this.outputType.setValue(outputType); }

    public void setInputFile(File inputFile) { this.inputFile.setValue(inputFile); }

    public void setMode(Mode mode) { this.mode.setValue(mode); }

    public void setOutputFilename(String outputFilename) { this.outputFilename.setValue(outputFilename); }

    public void setWriteToDifferentFile(boolean writeToDifferentFile) { this.writeToDifferentFile.setValue(writeToDifferentFile); }

    public void setNumberOfReplicates(short numberOfReplicates) { this.numberOfReplicates.setValue(numberOfReplicates); }

    public void setSortOption(SortOption sortOption) { this.sortOption.setValue(sortOption); }

    public void setEmptyInputSheetName(String emptyInputSheetName) { this.emptyInputSheetName.setValue(emptyInputSheetName); }

    public void setConditions(Set<Condition> conditions) { this.conditions.setValue(conditions); }

    public void setStrains(Set<Strain> strains) { this.strains.setValue(strains); }

    public void addCondition(Condition condition) { this.conditions.value().add(condition); }

    public void addStrain(Strain strain) { this.strains.value().add(strain); }

    public void addDay(short day) { this.days.value().add(day); }

    public void setDays(Set<Short> days) { this.days.setValue(days); }

    public void setNumDays(short numDays) { this.numDays.setValue(numDays); }

    public void setIncludeBaselineColumn(boolean includeBaselineColumn) { this.includeBaselineColumn.setValue(includeBaselineColumn); }
}
