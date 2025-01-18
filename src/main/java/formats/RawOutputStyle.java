package formats;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RawOutputStyle extends OutputStyle {
    public RawOutputStyle() {
        super(OutputType.RAW);
    }

    @Override
    public XSSFSheet[] generateOutputSheets() {
        // TODO: Implement
        return new XSSFSheet[0];
    }
}
