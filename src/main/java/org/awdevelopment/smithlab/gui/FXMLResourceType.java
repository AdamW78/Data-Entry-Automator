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
}
