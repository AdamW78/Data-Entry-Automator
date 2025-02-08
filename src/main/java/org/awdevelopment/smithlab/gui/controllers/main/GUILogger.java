package org.awdevelopment.smithlab.gui.controllers.main;

import javafx.scene.control.Label;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class GUILogger {
    final LoggerHelper LOGGER;

    GUILogger(LoggerHelper logger) {
        this.LOGGER = logger;
    }

    void errorOccurred(Label label, String message) {
        LOGGER.atError(message);
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    void clearError(Label label) { label.setText(""); }
}
