package org.awdevelopment.smithlab.io.output.formats;

import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BothOutputStyle extends OutputStyle {

    private final PrismOutputStyle prismOutputStyle;
    private final OtherOutputStyle otherOutputStyle;
    private final SortOption prismOutputSorting;

    public BothOutputStyle(SortOption prismOutputSorting) {
        super(OutputType.BOTH);
        prismOutputStyle = new PrismOutputStyle(prismOutputSorting);
        otherOutputStyle = new OtherOutputStyle();
        this.prismOutputSorting = prismOutputSorting;
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
