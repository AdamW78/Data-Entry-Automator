package org.awdevelopment.smithlab.gui;

import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

public class SceneLoader {

    private static final String STAGE_TITLE = "Smith Lab Data Entry Automator";

    public static void loadScene(Stage stage, FXMLResourceType fxmlResourceType, Logger logger) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType);
        fxmlResource.controller().setLogger(logger);
        stage.setTitle(STAGE_TITLE);
        stage.setScene(fxmlResource.scene());
        stage.show();
    }
}
