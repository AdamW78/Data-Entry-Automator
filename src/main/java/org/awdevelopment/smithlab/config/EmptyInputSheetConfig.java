package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.args.Arguments;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.logging.LoggerHelper;
import java.util.Set;

public class EmptyInputSheetConfig extends AbstractConfig {

    private static final Mode MODE = Mode.GENERATE_EMPTY_INPUT_SHEET;

    public EmptyInputSheetConfig(LoggerHelper logger) {
        super(logger);
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.SORT_OPTION, ConfigDefaults.SORT_OPTION));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, ConfigDefaults.NUMBER_OF_REPLICATES));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.OUTPUT_FILE, ConfigDefaults.OUTPUT_FILENAME));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.EMPTY_INPUT_SHEET_NAME, ConfigDefaults.EMPTY_INPUT_SHEET_NAME));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.CONDITIONS, ConfigDefaults.CONDITIONS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.STRAINS, ConfigDefaults.STRAINS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.DAYS, ConfigDefaults.DAYS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.INCLUDE_BASELINE_COLUMN, ConfigDefaults.INCLUDE_BASELINE_COLUMN));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.SAMPLE_LABELING_TYPE, ConfigDefaults.SAMPLE_LABELING_TYPE));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_DAYS, ConfigDefaults.NUM_DAYS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_CONDITIONS, ConfigDefaults.NUM_CONDITIONS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_STRAINS, ConfigDefaults.NUM_STRAINS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_DAYS, ConfigDefaults.USING_NUM_DAYS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_CONDITIONS, ConfigDefaults.USING_NUM_CONDITIONS));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_STRAINS, ConfigDefaults.USING_NUM_STRAINS));
    }

    public EmptyInputSheetConfig(Arguments args, LoggerHelper logger) {
        super(logger);
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.SORT_OPTION, args.getOutputSorting()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUMBER_OF_REPLICATES, args.getReplicateNumber()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.EMPTY_INPUT_SHEET_NAME, args.getEmptyInputSheetName()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.CONDITIONS, args.getConditions()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.STRAINS, args.getStrains()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.DAYS, args.getDays()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.INCLUDE_BASELINE_COLUMN, ConfigDefaults.INCLUDE_BASELINE_COLUMN));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.SAMPLE_LABELING_TYPE, args.getSampleLabelingType()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_DAYS, args.getNumDays()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_CONDITIONS, args.getNumConditions()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.NUM_STRAINS, args.getNumStrains()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_DAYS, args.usingNumDays()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_CONDITIONS, args.usingNumConditions()));
        super.addConfigEntry(new ConfigEntry<>(ConfigOption.USING_NUM_STRAINS, args.usingNumStrains()));
    }

    public Set<Condition> conditions() { return (Set<Condition>) get(ConfigOption.CONDITIONS); }
    public Set<Strain> strains() { return (Set<Strain>) get(ConfigOption.STRAINS); }
    public byte numReplicates() { return (byte) get(ConfigOption.NUMBER_OF_REPLICATES); }
    public String emptyInputSheetName() { return (String) get(ConfigOption.EMPTY_INPUT_SHEET_NAME); }
    public boolean includeBaselineColumn() { return (boolean) get(ConfigOption.INCLUDE_BASELINE_COLUMN); }
    public boolean usingNumDays() { return (boolean) get(ConfigOption.USING_NUM_DAYS); }
    public boolean usingNumConditions() { return (boolean) get(ConfigOption.USING_NUM_CONDITIONS); }
    public boolean usingNumStrains() { return (boolean) get(ConfigOption.USING_NUM_STRAINS); }
    public byte numDays() { return (byte) get(ConfigOption.NUM_DAYS); }
    public byte numConditions() { return (byte) get(ConfigOption.NUM_CONDITIONS); }
    public byte numStrains() { return (byte) get(ConfigOption.NUM_STRAINS); }
    public Set<Byte> days() { return (Set<Byte>) get(ConfigOption.DAYS); }
    public SortOption sortOption() { return (SortOption) get(ConfigOption.SORT_OPTION); }
    public SampleLabelingType sampleLabelingType() { return (SampleLabelingType) get(ConfigOption.SAMPLE_LABELING_TYPE); }
    public void setConditions(Set<Condition> conditions) { set(ConfigOption.CONDITIONS, conditions); }
    public void setStrains(Set<Strain> strains) { set(ConfigOption.STRAINS, strains); }
    public void setNumReplicates(byte numReplicates) { set(ConfigOption.NUMBER_OF_REPLICATES, numReplicates); }
    public void setEmptyInputSheetName(String emptyInputSheetName) { set(ConfigOption.EMPTY_INPUT_SHEET_NAME, emptyInputSheetName); }
    public void setIncludeBaselineColumn(boolean includeBaselineColumn) { set(ConfigOption.INCLUDE_BASELINE_COLUMN, includeBaselineColumn); }
    public void setUsingNumDays(boolean usingNumDays) { set(ConfigOption.USING_NUM_DAYS, usingNumDays); }
    public void setUsingNumConditions(boolean usingNumConditions) { set(ConfigOption.USING_NUM_CONDITIONS, usingNumConditions); }
    public void setUsingNumStrains(boolean usingNumStrains) { set(ConfigOption.USING_NUM_STRAINS, usingNumStrains); }
    public void setNumDays(byte numDays) { set(ConfigOption.NUM_DAYS, numDays); }
    public void setNumConditions(byte numConditions) { set(ConfigOption.NUM_CONDITIONS, numConditions); }
    public void setNumStrains(byte numStrains) { set(ConfigOption.NUM_STRAINS, numStrains); }
    public void setDays(Set<Byte> days) { set(ConfigOption.DAYS, days); }
    public void setSortOption(SortOption sortOption) { set(ConfigOption.SORT_OPTION, sortOption); }
    public void setSampleLabelingType(SampleLabelingType sampleLabelingType) { set(ConfigOption.SAMPLE_LABELING_TYPE, sampleLabelingType); }

    @Override
    public String toString() { return "Empty Input Sheet Configuration"; }
    @Override
    public Mode mode() { return MODE; };
}
