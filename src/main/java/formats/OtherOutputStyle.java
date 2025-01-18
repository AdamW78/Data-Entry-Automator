package formats;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OtherOutputStyle extends OutputStyle {

    public OtherOutputStyle() {
        super(OutputType.OTHER);
    }

    @Override
    public void generateOutputSheets(XSSFWorkbook workbook) {
        // TODO: implement
    }
}
