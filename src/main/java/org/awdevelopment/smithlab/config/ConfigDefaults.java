package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigDefaults {

    public static final byte NUM_DAYS = 0;
    public static final Object GUI = Boolean.TRUE;
    private static final String DEFAULT_FILENAME = "Spreadsheet Formatter Output.xlsx";
    public static final String EMPTY_INPUT_SHEET_FILENAME = "Empty Data Entry Sheet.xlsx";
    public static final OutputType OUTPUT_TYPE = OutputType.BOTH;
    public static final String OUTPUT_FILENAME = DEFAULT_FILENAME;
    public static final Mode MODE = Mode.GENERATE_OUTPUT_SHEETS;
    public static final boolean WRITE_TO_DIFFERENT_FILE = true;
    public static final SortOption SORT_OPTION = SortOption.SAMPLE_NUMBER;
    public static final File INPUT_FILE = null;
    public static final byte NUMBER_OF_REPLICATES = 4;
    public static final String EMPTY_INPUT_SHEET_NAME = DEFAULT_FILENAME;
    public static final Set<Condition> CONDITIONS = new HashSet<>();
    public static final Set<Strain> STRAINS = new HashSet<>();
    public static final Set<Byte> DAYS = new HashSet<>();
    public static final boolean INCLUDE_BASELINE_COLUMN = true;
    public static final SampleLabelingType SAMPLE_LABELING_TYPE = SampleLabelingType.CONDITION;
    public static final byte NUM_CONDITIONS = 0;
    public static final byte NUM_STRAINS = 0;
    public static final boolean USING_NUM_DAYS = false;
    public static final boolean USING_NUM_CONDITIONS = false;
    public static final boolean USING_NUM_STRAINS = false;

}
