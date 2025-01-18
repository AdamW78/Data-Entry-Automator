package formats;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RawOutputStyle extends OutputStyle {
    public RawOutputStyle() {
        super(OutputType.RAW);
    }

    @Override
    public void generateOutputSheets(XSSFWorkbook workbook) {
        // TODO: Implement
    }
}
