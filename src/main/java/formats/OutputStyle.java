package formats;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class OutputStyle {

    private final OutputType outputType;

    protected OutputStyle(OutputType outputType) {
        this.outputType = outputType;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public abstract XSSFSheet[] generateOutputSheets();
}
