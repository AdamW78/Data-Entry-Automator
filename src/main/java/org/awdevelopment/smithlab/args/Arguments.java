package org.awdevelopment.smithlab.args;

import org.awdevelopment.smithlab.args.exceptions.*;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.io.output.formats.OutputType;

import java.io.File;
import java.util.Scanner;

import static org.awdevelopment.smithlab.args.exceptions.InvalidReplicateNumberException.InvalidReplicateNumberReason.*;

public class Arguments {

    private static final OutputType DEFAULT_OUTPUT_TYPE = OutputType.BOTH;
    private static final String DEFAULT_OUTPUT_FILE_NAME = "Spreadsheet Formatter Output.xlsx";
    private static final Mode DEFAULT_MODE = Mode.GENERATE_OUTPUT_SHEETS;
    private static final boolean DEFAULT_WRITE_TO_DIFFERENT_FILE = false;
    private static final boolean DEFAULT_VERBOSE_VALUE = false;
    private static final SortOption DEFAULT_OUTPUT_SORTING = SortOption.SAMPLE_NUMBER;

    private OutputType outputType = DEFAULT_OUTPUT_TYPE;
    private Mode mode = DEFAULT_MODE;
    private boolean writeToDifferentFile = DEFAULT_WRITE_TO_DIFFERENT_FILE;
    private boolean verbose = DEFAULT_VERBOSE_VALUE;
    private String outputFileName = DEFAULT_OUTPUT_FILE_NAME;
    private File inputFile = null;
    private SortOption outputSorting = DEFAULT_OUTPUT_SORTING;
    private short replicateNumber = -1;

    public Arguments(String[] args) {
        try {
            readArguments(args);
        } catch (HelpException | NoSuchArgumentException | NoInputFileException e) {
            System.exit(0);
        } catch (InputFileNotFoundException | InvalidReplicateNumberException | NoReplicatesProvidedException e) {
            System.out.println(e.getMessage() + " Exiting...");
            System.exit(0);
        }
    }

    protected Arguments() {}

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
                    mode = Mode.valueOf(args[i + 1].toUpperCase());
                    i++;
                }
                case "--different", "-d" -> writeToDifferentFile = true;
                case "--output", "-o" -> {
                    checkIfHasNextArgument(args, i);
                    outputFileName = args[i + 1];
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
                case "--verbose", "-v" -> verbose = true;
                case "-h", "--help" -> {
                    printUsage();
                    throw new HelpException();
                }
                default -> {
                    System.out.println("Invalid argument: " + args[i]);
                    printUsage();
                    throw new NoSuchArgumentException(args[i]);
                }
            }
        }
        if (inputFile == null) {
            throw new NoInputFileException(args);
        }
        if (writeToDifferentFile && !suppliedOutputFileName) {
            System.out.println("Warning: Output file name not provided. Using default file name: " + outputFileName);
        }
        if (replicateNumber == -1) {
            if (outputType == OutputType.BOTH || outputType == OutputType.OTHER) {
                throw new NoReplicatesProvidedException(outputType);
            } else if (verbose) {
                System.out.println("WARNING: No replicate number provided. " +
                        "This should not matter for your currently-selected output type, \""+outputType+"\"," +
                        " but may be important for other output types (e.g. BOTH, OTHER).");
            }
        }
        if (suppliedOutputFileName && !writeToDifferentFile) {
            if (!inputFile.getPath().equals(outputFileName)) {
                System.out.println("Warning: Output file name provided but --different/-d flag not set. Will overwrite input file.");
                System.out.println("This behavior may change in the future.");
                System.out.println("To avoid this warning, use the --different/-d flag.");
                writeToDifferentFile = true;
            }
        }
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
                if (verbose) {
                        System.out.println("Warning: Invalid output sorting option: " + args[i + 1]);
                        System.out.println("Using default output sorting option: " + outputSorting);
                }
            }
        }
    }

    private void checkIfHasNextArgument(String[] args, int i) {
        if (i + 1 >= args.length) {
            throw new IllegalArgumentException("Missing argument after " + args[i]);
        }
    }

    private void checkOutputFileName() {
        if (outputFileName.startsWith("-")) {
            throw new IllegalArgumentException("Invalid output file name: " + outputFileName);
        } else if (!outputFileName.endsWith(".xlsx")) {
            System.out.println("Warning: Output file name does not end with .xlsx: " + outputFileName);
            System.out.println("Appending .xlsx to the end of the file name.");
            outputFileName += ".xlsx";
        }
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
        System.out.println("Warning: Output file already exists: " + outputFileName);
        System.out.print("Do you want to add sheets to the file? (y/N): ");
        String response = scanner.nextLine().strip();
        if (response.equalsIgnoreCase("y")) System.out.println("Adding sheets...");
        else {
            outputFileName = askForNewOutputFileName();
        }
        return outputFileName;
    }

    private String askForNewOutputFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a new output file name (ending with .xlsx): ");
        String newOutputFileName = scanner.nextLine().strip();
        if (newOutputFileName.isBlank()) {
            System.out.println("Blank file name entered. Please enter a valid file name.");
            return askForNewOutputFileName();
        }
        if (!newOutputFileName.endsWith(".xlsx")) {
            System.out.println("Warning: Output file name does not end with .xlsx: " + newOutputFileName);
            System.out.println("Appending .xlsx to the end of the file name.");
            newOutputFileName += ".xlsx";
        }
        if (fileExists(newOutputFileName)) {
            newOutputFileName = handleExistingOutputFile(newOutputFileName);
        }
        return newOutputFileName;
    }

    private String checkInputFileName(String inputFileName) {
       if (!inputFileName.endsWith(".xlsx")) {
            System.out.println("Warning: Input file name does not end with .xlsx: " + inputFileName);
            System.out.println("Appending .xlsx to the end of the file name.");
            inputFileName += ".xlsx";
        }
        return inputFileName;
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

    public boolean isVerbose() { return verbose; }

    private void printUsage() {
        System.out.println("Usage: java -jar SpreadsheetFormatter.jar [options]");
        System.out.println("Options:");
        System.out.println("  -i, --input <file>       Input file name (required)");
        System.out.println("  -t, --output-type <type> Output type (PRISM, OTHER, RAW, BOTH)");
        System.out.println("  -m, --mode <mode>        Mode (GENERATE_OUTPUT_SHEETS, GENERATE_STATE_SHEETS, GENERATE_STATE_TRANSITION_SHEETS)");
        System.out.println("  -d, --different          Write to a different file");
        System.out.println("  -o, --output <file>      Output file name");
        System.out.println("  -v, --verbose            Verbose output");
        System.out.println("  -h, --help               Print this help message");
    }

    public SortOption getOutputSorting() {
        return outputSorting;
    }
}
