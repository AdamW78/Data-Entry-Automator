package org.awdevelopment.smithlab.args;

import org.awdevelopment.smithlab.args.exceptions.*;
import org.awdevelopment.smithlab.config.ConfigDefault;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SampleLabelingType;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.output.formats.OutputType;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.awdevelopment.smithlab.args.exceptions.InvalidReplicateNumberException.InvalidReplicateNumberReason.*;

public class Arguments {

    private final LoggerHelper LOGGER;

    private OutputType outputType = ConfigDefault.OUTPUT_TYPE;
    private Mode mode = ConfigDefault.MODE;
    private boolean writeToDifferentFile = ConfigDefault.WRITE_TO_DIFFERENT_FILE;
    private String outputFileName = ConfigDefault.OUTPUT_FILENAME;
    private File inputFile = ConfigDefault.INPUT_FILE;
    private SortOption outputSorting = ConfigDefault.SORT_OPTION;
    private byte replicateNumber = ConfigDefault.NUMBER_OF_REPLICATES;
    private String emptyInputSheetName = ConfigDefault.EMPTY_INPUT_SHEET_NAME;
    private Set<Condition> conditions = ConfigDefault.CONDITIONS;
    private Set<Strain> strains = ConfigDefault.STRAINS;
    private Set<Byte> days = ConfigDefault.DAYS;
    private byte numDays = ConfigDefault.NUM_DAYS;
    private boolean includeBaselineColumn = ConfigDefault.INCLUDE_BASELINE_COLUMN;
    private SampleLabelingType sampleLabelingType = ConfigDefault.SAMPLE_LABELING_TYPE;
    private byte numConditions = ConfigDefault.NUM_CONDITIONS;
    private byte numStrains = ConfigDefault.NUM_STRAINS;
    private boolean usingNumDays = ConfigDefault.USING_NUM_DAYS;
    private boolean usingNumConditions = ConfigDefault.USING_NUM_CONDITIONS;
    private boolean usingNumStrains = ConfigDefault.USING_NUM_STRAINS;

    public Arguments(String[] args, LoggerHelper logger) {
        this.LOGGER = logger;
        try {
            readArguments(args);
        } catch (HelpException | NoSuchArgumentException | NoInputFileException e) {
            System.exit(0);
        } catch (InputFileNotFoundException | InvalidReplicateNumberException | NoReplicatesProvidedException | InvalidOutputFilenameException e) {
            LOGGER.atError(e.getMessage() + " - Exiting...");
            System.exit(0);
        }
    }

    protected Arguments(LoggerHelper logger) {
        this.LOGGER = logger;
    }

    protected void readArguments(String[] args) throws NoInputFileException, InputFileNotFoundException {
        boolean suppliedOutputFileName = false;
        if (args[0].equals("-h") || args[0].equals("--help") || args.length < 2) {
            printUsage();
            throw new HelpException();
        } else {
            parseArguments(args, suppliedOutputFileName);
        }
    }

    private void parseArguments(String[] args, boolean suppliedOutputFileName) throws InputFileNotFoundException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output-type", "-t" -> {
                    checkIfHasNextArgument(args, i);
                    outputType = OutputType.valueOf(args[i + 1].toUpperCase());
                    i++;
                }
                case "--mode", "-m" -> {
                    checkIfHasNextArgument(args, i);
                    mode = getModeFromArgument(args[i + 1]);
                    i++;
                }
                case "--different", "-d" -> writeToDifferentFile = true;
                case "--output", "-o" -> {
                    checkIfHasNextArgument(args, i);
                    outputFileName = args[i + 1];
                    emptyInputSheetName = outputFileName;
                    checkOutputFileName();
                    suppliedOutputFileName = true;
                    i++;
                }
                case "--input", "-i" -> {
                    checkIfHasNextArgument(args, i);
                    String inputFileName = checkInputFileName(args[i + 1]);
                    this.inputFile = new File(inputFileName);
                    checkInputFileExists(inputFileName);
                    i++;
                }
                case "--output-sorting" -> {
                    checkIfHasNextArgument(args, i);
                    checkOutputSorting(args, i);
                    i++;
                }
                case "--number-of-replicates", "-r" -> {
                    checkIfHasNextArgument(args, i);
                    checkReplicateNumber(args, i);
                    this.replicateNumber = Byte.parseByte(args[i + 1]);
                    i++;
                }
                case "--conditions" -> {
                    checkIfHasNextArgument(args, i);
                    conditions = readConditions(args[i + 1]);
                    i++;
                }
                case "--strains" -> {
                    checkIfHasNextArgument(args, i);
                    strains = readStrains(args[i + 1]);
                    i++;
                }
                case "--days" -> {
                    checkIfHasNextArgument(args, i);
                    days = readDays(args[i + 1]);
                    i++;
                }
                case "--include-baseline-column" -> includeBaselineColumn = true;
                case "--number-of-days", "--num-days" -> {
                    checkIfHasNextArgument(args, i);
                    numDays = readNumDays(args[i + 1]);
                    i++;
                }
                case "-h", "--help" -> {
                    printUsage();
                    throw new HelpException();
                }
                default -> {
                    LOGGER.atError("Invalid argument: " + args[i]);
                    printUsage();
                    throw new NoSuchArgumentException(args[i]);
                }
            }
        }
        if (!days.isEmpty() && numDays != 0) {
            LOGGER.atWarn("Both --days and --number-of-days were provided. --number-of-days will be ignored.");
            numDays = (byte) days.size();
        } else if (days.isEmpty() && numDays > 0) {
            LOGGER.atInfo("Number of days provided: " + numDays);
        }
        if (inputFile == null) {
            throw new NoInputFileException(args);
        }
        if (mode == Mode.GENERATE_EMPTY_INPUT_SHEET && conditions.isEmpty() && strains.isEmpty()) {
            LOGGER.atWarn("No conditions or strains provided. " +
                    "This may result in an empty input sheet.");
        }
        if (writeToDifferentFile) {
            if (!suppliedOutputFileName) LOGGER.atWarn("Output file name not provided. Using default file name: " + outputFileName);
            else if (outputFileName.equals(inputFile.getPath())) {
                writeToDifferentFile = false;
                LOGGER.atWarn("Output file name is the same as the input file name.");
            }
        }
        if (replicateNumber == -1) {
            if (outputType == OutputType.BOTH || outputType == OutputType.STATISTICAL_TESTS) {
                throw new NoReplicatesProvidedException(outputType);
            } else {
                LOGGER.atWarn("No replicate number provided. " +
                        "This should not matter for your currently-selected output type, \"" + outputType + "\"," +
                        " but may be important for other output types (e.g. Both, Statistical Tests).");
            }
        }
        if (suppliedOutputFileName && !writeToDifferentFile) {
            if (!inputFile.getPath().equals(outputFileName)) {
                LOGGER.atTrace(new String[]{"Warning: Output file name provided but --different/-d flag not set.",
                        "The Data Entry Automator will write output to file with name \""+ outputFileName+"\". " +
                                "This behavior may change in the future.",
                        "To avoid this warning, use the --different/-d flag."});
                writeToDifferentFile = true;
            }
        }
    }

    private byte readNumDays(String arg) {
        try {
            long numDays = Long.parseLong(arg);
            if (numDays <= 0) {
                LOGGER.atError("Number of days must be greater than 0: " + arg);
                return -1;
            } else if (numDays >= 128) {
                LOGGER.atError("Number of days must be less than 128: " + arg);
                return -1;
            }
            else return (byte) numDays;
        } catch (NumberFormatException e) {
            LOGGER.atError("Invalid number of days: \"" + arg + "\" - must be a positive integer < 128");
            return -1;
        }
    }

    private Set<Byte> readDays(String arg) {
        Set<Byte> days = new HashSet<>();
        String[] dayStrings = arg.split(",");
        for (String dayString : dayStrings) {
            try {
                byte day = Byte.parseByte(dayString);
                days.add(day);
            } catch (NumberFormatException e) {
                LOGGER.atError("Invalid day: " + dayString);
            }
        }
        return days;
    }

    private Set<Condition> readConditions(String arg) {
        Set<Condition> conditions = new HashSet<>();
        String[] conditionStrings = arg.split(",");
        for (String conditionString : conditionStrings) {
            Condition condition = new Condition(conditionString);
            conditions.add(condition);
        }
        return conditions;
    }

    private Set<Strain> readStrains(String arg) {
        Set<Strain> strains = new HashSet<>();
        String[] strainStrings = arg.split(",");
        for (String strainString : strainStrings) {
            Strain strain = new Strain(strainString);
            strains.add(strain);
        }
        return strains;
    }

    private Mode getModeFromArgument(String arg) {
        return switch (arg.toUpperCase().replaceAll("-", "").replaceAll("_", "")) {
            case "GENERATEOUTPUTSHEETS", "OUTPUTSHEETS", "OUTPUTSHEET", "OUTPUT" -> Mode.GENERATE_OUTPUT_SHEETS;
            case "GENERATEEMPTYINPUTSHEET", "INPUTSHEETS", "INPUTSHEET", "INPUT" -> Mode.GENERATE_EMPTY_INPUT_SHEET;
            default -> {
                LOGGER.atDebug(new String[] { "Warning: Invalid mode: " + arg, "Using default mode: " + mode });
                yield mode;
            }
        };
    }

    private void checkReplicateNumber(String[] args, int i) {
        try {
            long numberOfReplicates = Long.parseLong(args[i + 1]);
            if (numberOfReplicates <= 0) {
                throw new InvalidReplicateNumberException(LESS_THAN_ZERO);
            } else if (numberOfReplicates > 100) {
                throw new InvalidReplicateNumberException(GREATER_THAN_100);
            }
        } catch (NumberFormatException e) {
            throw new InvalidReplicateNumberException(NOT_AN_INTEGER);
        }
    }

    private void checkOutputSorting(String[] args, int i) {
        switch (args[i + 1].toUpperCase()) {
            case "ALPHABETICAL" -> outputSorting = SortOption.ALPHABETICAL;
            case "REVERSE_ALPHABETICAL" -> outputSorting = SortOption.REVERSE_ALPHABETICAL;
            case "SAMPLE, SAMPLE_NUMBER", "SAMPLE-NUMBER","SAMPLENUMBER","DEFAULT" -> outputSorting = SortOption.SAMPLE_NUMBER;
            case "NONE" -> outputSorting = SortOption.NONE;
            default -> LOGGER.atWarn(new String[] { "Warning: Invalid output sorting option: " + args[i + 1],
                        "Using default output sorting option: " + outputSorting });
        }
    }


    private void checkIfHasNextArgument(String[] args, int i) {
        if (i + 1 >= args.length) {
            LOGGER.atError("Error: No argument provided for " + args[i]);
            System.exit(0);
        }
    }

    private String checkFileExtension(String fileName) {
        if (!fileName.endsWith(".xlsx")) {
            LOGGER.atWarn(new String[] { "Warning: File name does not end with .xlsx: " + fileName,
                    "Appending .xlsx to the end of the file name." });
            fileName += ".xlsx";
        }
        return fileName;
    }

    private String checkFileName(String fileName) {
        if (fileName.isBlank()) {
            throw new FileNameException(fileName, FileNameException.FileNameExceptionType.EMPTY);
        } else
        if (fileName.startsWith("-")) {
            throw new FileNameException(fileName, FileNameException.FileNameExceptionType.INVALID_DASH);
        }
        return checkFileExtension(fileName);
    }

    private void checkOutputFileName() {
        if (outputFileName.startsWith("-")) {
            throw new IllegalArgumentException("Invalid output file name: " + outputFileName);
        }
        outputFileName = checkFileExtension(outputFileName);
        if (fileExists(outputFileName)) {
            outputFileName = handleExistingOutputFile(outputFileName);
        }
    }

    private boolean fileExists(String fileName) {
        File outputFile = new File(fileName);
        return outputFile.exists();
    }

    private String handleExistingOutputFile(String outputFileName) {
        Scanner scanner = new Scanner(System.in);
        LOGGER.atWarn("Output file already exists: \"" + outputFileName + "\"");
        System.out.print("Do you want to add sheets to the already existing file \"" + outputFileName + "\"? (y/N): ");
        String response = scanner.nextLine().strip();
        if (response.equalsIgnoreCase("y")
                || response.equalsIgnoreCase("yes")) LOGGER.atInfo("Adding sheets...");
        else outputFileName = askForNewOutputFileName();
        return outputFileName;
    }

    private String askForNewOutputFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Leave blank to exit.");
        System.out.println("Enter a new output file name (ending with .xlsx): ");
        String newOutputFileName = scanner.nextLine().strip();
        if (newOutputFileName.isBlank()) {
            LOGGER.atInfo("Blank file name entered. Exiting...");
            System.exit(0);
        }
        newOutputFileName = checkFileExtension(newOutputFileName);
        if (fileExists(newOutputFileName)) {
            newOutputFileName = handleExistingOutputFile(newOutputFileName);
        }
        return newOutputFileName;
    }

    private String checkInputFileName(String inputFileName) {
       return checkFileName(inputFileName);
    }

    private void checkInputFileExists(String inputFileName) throws InputFileNotFoundException {
        if (!inputFile.exists()) {
            throw new InputFileNotFoundException(inputFileName);
        }
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean writeToDifferentFile() {
        return writeToDifferentFile;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public File getInputFile() {
        return inputFile;
    }

    public byte getReplicateNumber() {
        return replicateNumber;
    }

    private void printUsage() {
        System.out.println("Usage: java -jar SpreadsheetFormatter.jar [options]");
        System.out.println("Options:");
        System.out.println("  -i, --input <file>       Input file name (required)");
        System.out.println("  -t, --output-type <type> Output type (PRISM, OTHER, RAW, BOTH)");
        System.out.println("  -m, --mode <mode>        Mode (GENERATE_OUTPUT_SHEETS, GENERATE_STATE_SHEETS, GENERATE_STATE_TRANSITION_SHEETS)");
        System.out.println("  -d, --different          Write to a different file");
        System.out.println("  -o, --output <file>      Output file name");
        System.out.println("  -h, --help               Print this help message");
    }

    public SortOption getOutputSorting() {
        return outputSorting;
    }

    public String getEmptyInputSheetName() { return emptyInputSheetName; }

    public Set<Condition> getConditions() { return conditions; }

    public Set<Strain> getStrains() { return strains; }

    public Set<Byte> getDays() { return days; }

    public byte getNumDays() { return numDays; }

    public Boolean getIncludeBaselineColumn() { return includeBaselineColumn; }

    public SampleLabelingType getSampleLabelingType() { return sampleLabelingType; }

    public byte getNumConditions() { return numConditions; }

    public byte getNumStrains() { return numStrains; }

    public boolean usingNumDays() { return usingNumDays; }

    public boolean usingNumConditions() { return usingNumConditions; }

    public boolean usingNumStrains() { return usingNumStrains; }
}
