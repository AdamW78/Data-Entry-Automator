package org.awdevelopment.smithlab.gui;

public class FailedToLoadFXMLException extends RuntimeException {

  private FailedToLoadFXMLException(String message) {
        super(message);
    }

  public FailedToLoadFXMLException(FXMLResourceType fxmlResource) {
        switch (fxmlResource) {
            case GENERATE_EMPTY_INPUT_SHEET ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"generate_empty_input_sheet.fxml\"");
            case GENERATE_OUTPUT_SHEETS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"generate_output_sheets.fxml\"");
        }
    }
}
