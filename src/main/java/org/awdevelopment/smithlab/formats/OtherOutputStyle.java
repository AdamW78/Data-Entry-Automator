package org.awdevelopment.smithlab.formats;

import org.awdevelopment.smithlab.data.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class OtherOutputStyle extends OutputStyle {

    public OtherOutputStyle() {
        super(OutputType.OTHER);
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        // TODO: implement
    }
}
