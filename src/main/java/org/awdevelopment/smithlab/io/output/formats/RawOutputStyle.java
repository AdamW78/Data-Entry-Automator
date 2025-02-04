package org.awdevelopment.smithlab.io.output.formats;

import org.awdevelopment.smithlab.config.SortOption;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RawOutputStyle extends OutputStyle {
    public RawOutputStyle(SortOption sortOption) {
        super(OutputType.RAW, sortOption);
    }

    @Override
    public void generateOutputSheets(XSSFSheet[] sheets, Experiment experiment) {
        // TODO: Implement
    }
}
