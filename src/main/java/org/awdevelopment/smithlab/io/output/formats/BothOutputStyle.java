package org.awdevelopment.smithlab.io.output.formats;

import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BothOutputStyle extends OutputStyle {

    private final PrismOutputStyle prismOutputStyle;
    private final OtherOutputStyle otherOutputStyle;

    public BothOutputStyle(SortOption sortOption, short numberOfReplicates) {
        super(OutputType.BOTH, sortOption);
        prismOutputStyle = new PrismOutputStyle(sortOption);
        otherOutputStyle = new OtherOutputStyle(sortOption, numberOfReplicates);
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        XSSFSheet[] sheetsPrism = new XSSFSheet[1];
        sheetsPrism[0] = sheets[sheets.length - 2];
        XSSFSheet[] sheetsOther = new XSSFSheet[1];
        sheetsOther[0] = sheets[sheets.length - 1];
        prismOutputStyle.generateOutputSheets(sheetsPrism, experiment);
        otherOutputStyle.generateOutputSheets(sheetsOther, experiment);
    }

}
