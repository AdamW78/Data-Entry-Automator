package formats;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PrismOutputStyle extends OutputStyle {
    public PrismOutputStyle() {
        super(OutputType.PRISM);
    }


    @Override
    public XSSFSheet[] generateOutputSheets() {
        // TODO: implement
        return null;
    }
}
