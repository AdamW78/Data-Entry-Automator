import formats.OutputType;

import java.io.File;
import java.util.Scanner;

public class Arguments {

    private static final OutputType DEFAULT_OUTPUT_TYPE = OutputType.BOTH;
    private static final String DEFAULT_OUTPUT_FILE_NAME = "Spreadsheet Formatter Output.xlsx";
    private static final Mode DEFAULT_MODE = Mode.GENERATE_OUTPUT_SHEETS;
    private static final boolean DEFAULT_WRITE_TO_DIFFERENT_FILE = false;

    private OutputType outputType = DEFAULT_OUTPUT_TYPE;
    private Mode mode = DEFAULT_MODE;
    private boolean writeToDifferentFile = DEFAULT_WRITE_TO_DIFFERENT_FILE;
    private String outputFileName = DEFAULT_OUTPUT_FILE_NAME;
    private File inputFile = null;

    public Arguments(String[] args) {
        readArguments(args);
    }

    private void readArguments(String[] args) {
        boolean suppliedOutputFileName = false;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output-type", "-t" -> {
                    checkIfHasNextArgument(args, i);
                    outputType = OutputType.valueOf(args[i + 1].toUpperCase());
                }
                case "--mode", "-m" -> {
                    checkIfHasNextArgument(args, i);
                    mode = Mode.valueOf(args[i + 1].toUpperCase());
                }
                case "--different", "-d" -> writeToDifferentFile = true;
                case "--output", "-o" -> {
                    checkIfHasNextArgument(args, i);
                    outputFileName = args[i + 1];
                    checkOutputFileName();
                    suppliedOutputFileName = true;
                }
                case "--input", "-i" -> {
                    checkIfHasNextArgument(args, i);
                    String inputFileName = checkInputFileName(args[i + 1]);
                    this.inputFile = new File(inputFileName);
                    checkInputFileExists(inputFileName);
                }
                default -> throw new IllegalArgumentException("Invalid argument: " + args[i]);
            }
        }
        if (inputFile == null) {
            System.out.println("Fatal error: No input file provided. Exiting program.");
            System.exit(1);
        }
        if (writeToDifferentFile && !suppliedOutputFileName) {
            System.out.println("Warning: Output file name not provided. Using default file name: " + outputFileName);
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
        String response = scanner.nextLine();
        if (!response.equalsIgnoreCase("y")) System.out.println("Adding sheets...");
        else {
            outputFileName = askForNewOutputFileName();
        }
        return outputFileName;
    }

    private String askForNewOutputFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a new output file name (ending with .xlsx): ");
        String newOutputFileName = scanner.nextLine();
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

    private void checkInputFileExists(String inputFileName) {
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("File not found: " + inputFileName + " does not exist.");
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
}
