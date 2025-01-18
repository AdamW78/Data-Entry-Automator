package formats;

import data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BothOutputStyle extends OutputStyle {

    private final PrismOutputStyle prismOutputStyle;
    private final OtherOutputStyle otherOutputStyle;

    public BothOutputStyle() {
        super(OutputType.BOTH);
        prismOutputStyle = new PrismOutputStyle();
        otherOutputStyle = new OtherOutputStyle();
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        XSSFSheet[] sheetsPrism = new XSSFSheet[1];
        sheetsPrism[0] = sheets[0];
        XSSFSheet[] sheetsOther = new XSSFSheet[1];
        sheetsOther[0] = sheets[1];
        prismOutputStyle.generateOutputSheets(sheetsPrism, experiment);
        otherOutputStyle.generateOutputSheets(sheetsOther, experiment);
    }

}
