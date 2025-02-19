package org.awdevelopment.smithlab.gui;

import javafx.stage.Stage;
import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.gui.controllers.AbstractLabelController;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class SceneLoader {

    public static void loadScene(Stage stage, FXMLResourceType fxmlResourceType, LoggerHelper logger) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType, logger);
        fxmlResource.controller().setLogger(logger);
        fxmlResource.controller().setup();
        stage.setTitle(fxmlResourceType.getWindowTitle());
        stage.setScene(fxmlResource.scene());
        stage.show();
    }

    public static AbstractLabelController loadScene(Stage stage, FXMLResourceType fxmlResourceType, LoggerHelper logger, EmptyInputSheetConfig config) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType, logger);
        fxmlResource.controller().setLogger(logger);
        ((AbstractLabelController) fxmlResource.controller()).setConfig(config);
        fxmlResource.controller().setup();
        stage.setTitle(fxmlResourceType.getWindowTitle());
        stage.setScene(fxmlResource.scene());
        stage.show();
        return (AbstractLabelController) fxmlResource.controller();
    }
}
