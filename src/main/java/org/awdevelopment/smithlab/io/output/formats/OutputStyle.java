package org.awdevelopment.smithlab.io.output.formats;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;

public abstract class OutputStyle {

    private final OutputType outputType;
    protected final SortOption sortOption;

    protected OutputStyle(OutputType outputType, SortOption sortOption) {
        this.outputType = outputType;
        this.sortOption = sortOption;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public void generateLabels(XSSFSheet[] sheets, Experiment experiment) throws NoStrainsOrConditionsException {
        XSSFSheet sheet = sheets[sheets.length - 1];
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        if (experiment.hasBothStrainsAndConditions()) {
            cell.setCellValue("Strain + Condition");
        } else if (experiment.hasStrains()) {
            cell.setCellValue("Strain");
        } else if (experiment.hasConditions()) {
            cell.setCellValue("Condition");
        } else {
            throw new NoStrainsOrConditionsException();
        }
    }

    protected String getStringCellAddress(XSSFCell cell) {
        return cell.getAddress().formatAsString();
    }

    public abstract void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) throws NoStrainsOrConditionsException;
}
