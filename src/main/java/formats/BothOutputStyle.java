package formats;

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
    public XSSFSheet[] generateOutputSheets() {
        XSSFSheet[] prismSheets = prismOutputStyle.generateOutputSheets();
        XSSFSheet[] otherSheets = otherOutputStyle.generateOutputSheets();
        XSSFSheet[] bothSheets = new XSSFSheet[prismSheets.length + otherSheets.length];
        System.arraycopy(prismSheets, 0, bothSheets, 0, prismSheets.length);
        System.arraycopy(otherSheets, 0, bothSheets, prismSheets.length, otherSheets.length);
        return bothSheets;
    }

}
