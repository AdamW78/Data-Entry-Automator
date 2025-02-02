package org.awdevelopment.smithlab.gui;

import javafx.stage.Stage;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class SceneLoader {

    private static final String STAGE_TITLE = "Smith Lab Data Entry Automator";

    public static void loadScene(Stage stage, FXMLResourceType fxmlResourceType, LoggerHelper logger) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType, logger);
        fxmlResource.controller().setLogger(logger);
        stage.setTitle(STAGE_TITLE);
        stage.setScene(fxmlResource.scene());
        stage.show();
    }
}
