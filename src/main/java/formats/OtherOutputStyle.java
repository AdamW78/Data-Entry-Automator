package formats;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class OtherOutputStyle extends OutputStyle {

    public OtherOutputStyle() {
        super(OutputType.OTHER);
    }

    @Override
    public XSSFSheet[] generateOutputSheets() {
        // TODO: implement
        return null;
    }
}
