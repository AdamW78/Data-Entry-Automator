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
    private final ConfigEntry<Byte> numberOfReplicates;
    private final ConfigEntry<String> emptyInputSheetName;
    private final ConfigEntry<Boolean> GUI;
    private final ConfigEntry<Set<Condition>> conditions;
    private final ConfigEntry<Set<Strain>> strains;
    private final ConfigEntry<Set<Byte>> days;
    private final ConfigEntry<Byte> numDays;
    private final ConfigEntry<Boolean> includeBaselineColumn;
    private final ConfigEntry<SampleLabelingType> sampleLabelingType;
    private final ConfigEntry<Byte> numConditions;
    private final ConfigEntry<Byte> numStrains;
    private final ConfigEntry<Boolean> usingNumDays;
    private final ConfigEntry<Boolean> usingNumConditions;
    private final ConfigEntry<Boolean> usingNumStrains;

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
        this.sampleLabelingType = new ConfigEntry<>(ConfigOption.SAMPLE_LABELING_TYPE, ConfigDefault.SAMPLE_LABELING_TYPE);
        this.numConditions = new ConfigEntry<>(ConfigOption.NUM_CONDITIONS, ConfigDefault.NUM_CONDITIONS);
        this.numStrains = new ConfigEntry<>(ConfigOption.NUM_STRAINS, ConfigDefault.NUM_STRAINS);
        this.usingNumDays = new ConfigEntry<>(ConfigOption.USING_NUM_DAYS, ConfigDefault.USING_NUM_DAYS);
        this.usingNumConditions = new ConfigEntry<>(ConfigOption.USING_NUM_CONDITIONS, ConfigDefault.USING_NUM_CONDITIONS);
        this.usingNumStrains = new ConfigEntry<>(ConfigOption.USING_NUM_STRAINS, ConfigDefault.USING_NUM_STRAINS);
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
        this.sampleLabelingType = new ConfigEntry<>(ConfigOption.SAMPLE_LABELING_TYPE, arguments.getSampleLabelingType());
        this.numConditions = new ConfigEntry<>(ConfigOption.NUM_CONDITIONS, arguments.getNumConditions());
        this.numStrains = new ConfigEntry<>(ConfigOption.NUM_STRAINS, arguments.getNumStrains());
        this.usingNumDays = new ConfigEntry<>(ConfigOption.USING_NUM_DAYS, arguments.usingNumDays());
        this.usingNumConditions = new ConfigEntry<>(ConfigOption.USING_NUM_CONDITIONS, arguments.usingNumConditions());
        this.usingNumStrains = new ConfigEntry<>(ConfigOption.USING_NUM_STRAINS, arguments.usingNumStrains());
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

    public byte numReplicates() { return numberOfReplicates.value(); }

    public SortOption sortOption() { return sortOption.value(); }

    public String emptyInputSheetName() { return emptyInputSheetName.value(); }

    public boolean GUI() { return GUI.value(); }

    public Set<Condition> conditions() { return conditions.value(); }

    public Set<Strain> strains() { return strains.value(); }

    public Set<Byte> days() { return days.value(); }

    public byte numDays() { return numDays.value(); }

    public boolean includeBaselineColumn() { return includeBaselineColumn.value(); }

    public SampleLabelingType sampleLabelingType() { return sampleLabelingType.value(); }

    public byte numConditions() { return numConditions.value(); }

    public byte numStrains() { return numStrains.value(); }

    public boolean usingNumDays() { return usingNumDays.value(); }

    public boolean usingNumConditions() { return usingNumConditions.value(); }

    public boolean usingNumStrains() { return usingNumStrains.value(); }

    public void setOutputType(OutputType outputType) { this.outputType.setValue(outputType); }

    public void setInputFile(File inputFile) { this.inputFile.setValue(inputFile); }

    public void setMode(Mode mode) { this.mode.setValue(mode); }

    public void setOutputFilename(String outputFilename) { this.outputFilename.setValue(outputFilename); }

    public void setWriteToDifferentFile(boolean writeToDifferentFile) { this.writeToDifferentFile.setValue(writeToDifferentFile); }

    public void setNumberOfReplicates(byte numberOfReplicates) { this.numberOfReplicates.setValue(numberOfReplicates); }

    public void setSortOption(SortOption sortOption) { this.sortOption.setValue(sortOption); }

    public void setEmptyInputSheetName(String emptyInputSheetName) { this.emptyInputSheetName.setValue(emptyInputSheetName); }

    public void setConditions(Set<Condition> conditions) { this.conditions.setValue(conditions); }

    public void setStrains(Set<Strain> strains) { this.strains.setValue(strains); }

    public void addCondition(Condition condition) { this.conditions.value().add(condition); }

    public void addStrain(Strain strain) { this.strains.value().add(strain); }

    public void addDay(byte day) { this.days.value().add(day); }

    public void setDays(Set<Byte> days) { this.days.setValue(days); }

    public void setNumDays(byte numDays) { this.numDays.setValue(numDays); }

    public void setIncludeBaselineColumn(boolean includeBaselineColumn) { this.includeBaselineColumn.setValue(includeBaselineColumn); }

    public void setSampleLabelingType(SampleLabelingType sampleLabelingType) { this.sampleLabelingType.setValue(sampleLabelingType); }

    public void setNumConditions(byte numConditions) { this.numConditions.setValue(numConditions); }

    public void setNumStrains(byte numStrains) { this.numStrains.setValue(numStrains); }

    public void setUsingNumDays(boolean usingNumDays) { this.usingNumDays.setValue(usingNumDays); }

    public void setUsingNumConditions(boolean usingNumConditions) { this.usingNumConditions.setValue(usingNumConditions); }

    public void setUsingNumStrains(boolean usingNumStrains) { this.usingNumStrains.setValue(usingNumStrains); }
}
