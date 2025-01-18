package formats;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BothOutputStyle extends OutputStyle {

    private final PrismOutputStyle prismOutputStyle;
    private final OtherOutputStyle otherOutputStyle;

    public BothOutputStyle() {
        super(OutputType.BOTH);
        prismOutputStyle = new PrismOutputStyle();
        otherOutputStyle = new OtherOutputStyle();
    }

    @Override
    public void generateOutputSheets(XSSFWorkbook workbook) {
        prismOutputStyle.generateOutputSheets(workbook);
        otherOutputStyle.generateOutputSheets(workbook);
    }

}
