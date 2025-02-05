package org.awdevelopment.smithlab.gui;

public enum FXMLResourceType {
    APPLICATION, CONDITIONS, STRAINS, TIMEPOINTS;

    @Override
    public String toString() { return getPath(this); }

    private static String getPath(FXMLResourceType fxmlResource) {
        return switch (fxmlResource) {
            case APPLICATION -> "/fxml/application.fxml";
            case CONDITIONS -> "/fxml/conditions.fxml";
            case STRAINS -> "/fxml/strains.fxml";
            case TIMEPOINTS -> "/fxml/timepoints.fxml";
        };
    }

    public String getWindowTitle() {
        return switch (this) {
            case APPLICATION -> "Smith Lab Data Entry Automator";
            case CONDITIONS -> "Conditions";
            case STRAINS -> "Strains";
            case TIMEPOINTS -> "Timepoints";
        };
    }
}
