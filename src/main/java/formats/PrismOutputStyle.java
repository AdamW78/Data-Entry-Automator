package formats;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PrismOutputStyle extends OutputStyle {
    public PrismOutputStyle() {
        super(OutputType.PRISM);
    }


    @Override
    public void generateOutputSheets(XSSFWorkbook workbook) {
        // TODO: implement
    }
}
