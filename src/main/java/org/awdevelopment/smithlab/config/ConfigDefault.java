package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigDefault {

    public static final Short NUM_DAYS = 0;
    private static final String DEFAULT_FILENAME = "Spreadsheet Formatter Output.xlsx";
    public static final OutputType OUTPUT_TYPE = OutputType.BOTH;
    public static final String OUTPUT_FILENAME = DEFAULT_FILENAME;
    public static final Mode MODE = Mode.GENERATE_OUTPUT_SHEETS;
    public static final boolean WRITE_TO_DIFFERENT_FILE = true;
    public static final SortOption SORT_OPTION = SortOption.SAMPLE_NUMBER;
    public static final File INPUT_FILE = null;
    public static final short NUMBER_OF_REPLICATES = 4;
    public static final String EMPTY_INPUT_SHEET_NAME = DEFAULT_FILENAME;
    public static final Set<Condition> CONDITIONS = new HashSet<>();
    public static final Set<Strain> STRAINS = new HashSet<>();
    public static final Set<Short> DAYS = new HashSet<>();
    public static final boolean INCLUDE_BASELINE_COLUMN = true;

}
