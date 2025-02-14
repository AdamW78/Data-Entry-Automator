package org.awdevelopment.smithlab.logging;

import javafx.scene.control.Label;

public class GUILogger {
    final LoggerHelper LOGGER;

    public GUILogger(LoggerHelper logger) {
        this.LOGGER = logger;
    }

    public void errorOccurred(Label label, String message) {
        LOGGER.atError(message);
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    public void clearError(Label label) { label.setText(""); }
}
