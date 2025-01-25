package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;

public class ConfigDefault {

    private static final String DEFAULT_FILENAME = "Spreadsheet Formatter Output.xlsx";
    public static final OutputType OUTPUT_TYPE = OutputType.BOTH;
    public static final String OUTPUT_FILENAME = DEFAULT_FILENAME;
    public static final Mode MODE = Mode.GENERATE_OUTPUT_SHEETS;
    public static final boolean WRITE_TO_DIFFERENT_FILE = false;
    public static final SortOption SORT_OPTION = SortOption.SAMPLE_NUMBER;
    public static final File INPUT_FILE = null;
    public static final short NUMBER_OF_REPLICATES = -1;
    public static final String EMPTY_INPUT_SHEET_NAME = DEFAULT_FILENAME;

}
