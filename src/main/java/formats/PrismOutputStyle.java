package formats;

import data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PrismOutputStyle extends OutputStyle {
    public PrismOutputStyle() {
        super(OutputType.PRISM);
    }


    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        // TODO: implement
    }
}
