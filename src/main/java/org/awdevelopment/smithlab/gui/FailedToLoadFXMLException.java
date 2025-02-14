package org.awdevelopment.smithlab.gui;

public class FailedToLoadFXMLException extends RuntimeException {

  private FailedToLoadFXMLException(String message, Exception e) {
        super(message, e);
    }

  public FailedToLoadFXMLException(FXMLResourceType fxmlResource, Exception e) {
        switch (fxmlResource) {
            case FXMLResourceType.APPLICATION ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"application.fxml\"", e);
            case FXMLResourceType.CONDITIONS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"conditions.fxml\"", e);
            case FXMLResourceType.STRAINS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"strains.fxml\"", e);
            case FXMLResourceType.TIMEPOINTS ->
                    throw new FailedToLoadFXMLException("Failed to load .fxml file \"timepoints.fxml\"", e);
        }
    }
}
