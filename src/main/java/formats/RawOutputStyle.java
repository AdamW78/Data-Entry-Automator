package formats;

import data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RawOutputStyle extends OutputStyle {
    public RawOutputStyle() {
        super(OutputType.RAW);
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        // TODO: Implement
    }
}
