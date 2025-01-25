package org.awdevelopment.smithlab.args;

import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.args.exceptions.*;
import org.awdevelopment.smithlab.config.ConfigDefault;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;
import java.util.Scanner;

import static org.awdevelopment.smithlab.args.exceptions.InvalidReplicateNumberException.InvalidReplicateNumberReason.*;

public class Arguments {

    private final Logger LOGGER;

    private OutputType outputType = ConfigDefault.OUTPUT_TYPE;
    private Mode mode = ConfigDefault.MODE;
    private boolean writeToDifferentFile = ConfigDefault.WRITE_TO_DIFFERENT_FILE;
    private String outputFileName = ConfigDefault.OUTPUT_FILENAME;
    private File inputFile = ConfigDefault.INPUT_FILE;
    private SortOption outputSorting = ConfigDefault.SORT_OPTION;
    private short replicateNumber = ConfigDefault.NUMBER_OF_REPLICATES;
    private String emptyInputSheetName = ConfigDefault.EMPTY_INPUT_SHEET_NAME;

    public Arguments(String[] args, Logger logger) {
        this.LOGGER = logger;
        try {
            readArguments(args);
        } catch (HelpException | NoSuchArgumentException | NoInputFileException e) {
            System.exit(0);
        } catch (InputFileNotFoundException | InvalidReplicateNumberException | NoReplicatesProvidedException e) {
            String logMessage = e.getMessage() + " Exiting...";
            LOGGER.atError().log(logMessage);
            System.exit(0);
        }
    }

    protected Arguments(Logger logger) {
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
                    this.replicateNumber = Short.parseShort(args[i + 1]);
                    i++;
                }
                case "-h", "--help" -> {
                    printUsage();
                    throw new HelpException();
                }
                default -> {
                    String logMessage = "Invalid argument: " + args[i];
                    LOGGER.atError().log(logMessage);
                    printUsage();
                    throw new NoSuchArgumentException(args[i]);
                }
            }
        }
        if (inputFile == null) {
            throw new NoInputFileException(args);
        }
        if (writeToDifferentFile) {
            if (!suppliedOutputFileName) {
                String logMessage = "Output file name not provided. Using default file name: " + outputFileName;
                LOGGER.atWarn().log(logMessage);
            } else if (outputFileName.equals(inputFile.getPath())) {
                writeToDifferentFile = false;
                LOGGER.atWarn().log("Output file name is the same as the input file name.");
            }
        }
        if (replicateNumber == -1) {
            if (outputType == OutputType.BOTH || outputType == OutputType.OTHER) {
                throw new NoReplicatesProvidedException(outputType);
            } else {
                String logMessage = "No replicate number provided. " +
                        "This should not matter for your currently-selected output type, \"" + outputType + "\"," +
                        " but may be important for other output types (e.g. BOTH, OTHER).";
                LOGGER.atWarn().log(logMessage);
            }
        }
        if (suppliedOutputFileName && !writeToDifferentFile) {
            if (!inputFile.getPath().equals(outputFileName)) {
                String[] logMessage = {"Warning: Output file name provided but --different/-d flag not set.",
                        "The Data Entry Automator will write output to file with name \""+ outputFileName+"\". This behavior may change in the future.",
                        "To avoid this warning, use the --different/-d flag."};
                for (String message : logMessage) LOGGER.atWarn().log(message);
                writeToDifferentFile = true;
            }
        }
    }

    private Mode getModeFromArgument(String arg) {
        return switch (arg.toUpperCase().replaceAll("-", "").replaceAll("_", "")) {
            case "GENERATEOUTPUTSHEETS", "OUTPUTSHEETS", "OUTPUTSHEET", "OUTPUT" -> Mode.GENERATE_OUTPUT_SHEETS;
            case "GENERATEEMPTYINPUTSHEET", "INPUTSHEETS", "INPUTSHEET", "INPUT" -> Mode.GENERATE_EMPTY_INPUT_SHEET;
            default -> {
                String[] logMessages = { "Warning: Invalid mode: " + arg,
                        "Using default mode: " + mode };
                for (String message : logMessages) LOGGER.atWarn().log(message);
                yield mode;
            }
        };
    }

    private void checkReplicateNumber(String[] args, int i) {
        try {
            int numberOfReplicates = Short.parseShort(args[i + 1]);
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
            default -> {
                String[] logMessages = { "Warning: Invalid output sorting option: " + args[i + 1],
                        "Using default output sorting option: " + outputSorting };
                for (String message : logMessages) LOGGER.atWarn().log(message);
            }
        }
    }


    private void checkIfHasNextArgument(String[] args, int i) {
        if (i + 1 >= args.length) {
            String logMessage = "Error: No argument provided for " + args[i];
            LOGGER.atError().log(logMessage);
            System.exit(0);
        }
    }

    private String checkFileExtension(String fileName) {
        if (!fileName.endsWith(".xlsx")) {
            String[] logMessages = { "Warning: File name does not end with .xlsx: " + fileName,
                    "Appending .xlsx to the end of the file name." };
            for (String message : logMessages) LOGGER.atWarn().log(message);
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
        String logMessage = "Output file already exists: " + outputFileName;
        LOGGER.atWarn().log(logMessage);
        System.out.print("Do you want to add sheets to the already existing file \"" + outputFileName + "\"? (y/N): ");
        String response = scanner.nextLine().strip();
        if (response.equalsIgnoreCase("y")
                || response.equalsIgnoreCase("yes")) LOGGER.atInfo().log("Adding sheets...");
        else outputFileName = askForNewOutputFileName();
        return outputFileName;
    }

    private String askForNewOutputFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Leave blank to exit.");
        System.out.println("Enter a new output file name (ending with .xlsx): ");
        String newOutputFileName = scanner.nextLine().strip();
        if (newOutputFileName.isBlank()) {
            LOGGER.atInfo().log("Blank file name entered. Exiting...");
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

    public short getReplicateNumber() {
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
}
