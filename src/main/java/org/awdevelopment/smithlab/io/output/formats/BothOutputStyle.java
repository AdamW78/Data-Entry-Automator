package org.awdevelopment.smithlab.io.output.formats;

import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.awdevelopment.smithlab.io.exceptions.NoStrainsOrConditionsException;

public class BothOutputStyle extends OutputStyle {

    private final PrismOutputStyle prismOutputStyle;
    private final StatisticalTestsOutputStyle statisticalTestsOutputStyle;

    public BothOutputStyle(SortOption sortOption, short numberOfReplicates) {
        super(OutputType.BOTH, sortOption);
        prismOutputStyle = new PrismOutputStyle(sortOption);
        statisticalTestsOutputStyle = new StatisticalTestsOutputStyle(sortOption, numberOfReplicates);
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) throws NoStrainsOrConditionsException {
        XSSFSheet[] sheetsPrism = new XSSFSheet[1];
        sheetsPrism[0] = sheets[sheets.length - 2];
        XSSFSheet[] sheetsOther = new XSSFSheet[1];
        sheetsOther[0] = sheets[sheets.length - 1];
        prismOutputStyle.generateOutputSheets(sheetsPrism, experiment);
        statisticalTestsOutputStyle.generateOutputSheets(sheetsOther, experiment);
    }

}
