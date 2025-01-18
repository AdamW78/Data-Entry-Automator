package io.output;

import data.Experiment;
import formats.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class OutputGenerator {

    private final OutputStyle outputStyle;
    private String outputFileName;

    public OutputGenerator(OutputType outputType, boolean writeToDifferentFile, String outputFileName, File inputFile) {
        switch (outputType) {
            case PRISM:
                outputStyle = new PrismOutputStyle();
                break;
            case OTHER:
                outputStyle = new OtherOutputStyle();
                break;
            case RAW:
                outputStyle = new RawOutputStyle();
                break;
            case BOTH:
                outputStyle = new BothOutputStyle();
                break;
            default:
                throw new IllegalArgumentException("Invalid output type: " + outputType);
        }
        if (writeToDifferentFile) {
            this.outputFileName = outputFileName;
        } else {
            this.outputFileName = inputFile.getName();
        }
    }

    public void generateOutput(Experiment experiment) throws IOException, InvalidFormatException {
        XlsxOutputWriter writer = new XlsxOutputWriter();
        writer.writeOutput(outputFileName);
        // TODO: write the output sheets to a file
    }


}
