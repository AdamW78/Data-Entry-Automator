package org.awdevelopment.smithlab.gui;

public class FailedToLoadFXMLException extends RuntimeException {

  private FailedToLoadFXMLException(String message) {
        super(message);
    }

  public FailedToLoadFXMLException(FXMLResourceType fxmlResource) {
        switch (fxmlResource) {
            case FXMLResourceType.APPLICATION ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"application.fxml\"");
            case FXMLResourceType.CONDITIONS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"conditions.fxml\"");
            case FXMLResourceType.STRAINS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"strains.fxml\"");
            case FXMLResourceType.TIMEPOINTS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"timepoints.fxml\"");
        }
    }
}
