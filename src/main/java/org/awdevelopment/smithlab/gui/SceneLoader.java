package org.awdevelopment.smithlab.gui;

import javafx.stage.Stage;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.gui.controllers.AbstractLabelController;
import org.awdevelopment.smithlab.logging.LoggerHelper;

public class SceneLoader {

    public static void loadScene(Stage stage, FXMLResourceType fxmlResourceType, LoggerHelper logger) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType, logger);
        fxmlResource.controller().setLogger(logger);
        stage.setTitle(fxmlResourceType.getWindowTitle());
        stage.setScene(fxmlResource.scene());
        stage.show();
    }

    public static AbstractLabelController loadScene(Stage stage, FXMLResourceType fxmlResourceType, LoggerHelper logger, Config config) {
        FXMLResource fxmlResource = FXMLResourceLoader.load(fxmlResourceType, logger);
        fxmlResource.controller().setLogger(logger);
        ((AbstractLabelController) fxmlResource.controller()).setConfig(config);
        stage.setTitle(fxmlResourceType.getWindowTitle());
        stage.setScene(fxmlResource.scene());
        stage.show();
        return (AbstractLabelController) fxmlResource.controller();
    }
}
