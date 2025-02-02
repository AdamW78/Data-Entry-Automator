package org.awdevelopment.smithlab.gui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.gui.controllers.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;

public class FXMLResourceLoader {

    private static final Level LOAD_MESSAGE_LOG_LEVEL = Level.DEBUG;

    public static FXMLResource load(FXMLResourceType fxmlResource, Logger logger) {
        Scene scene;
        AbstractController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getFxmlResourcePath(fxmlResource, logger));
            scene = new Scene(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            loggerHelper(logger, "Failed to load FXML resource: \"" + fxmlResource+"\"", e);
            throw new FailedToLoadFXMLException(fxmlResource);
        }
        return new FXMLResource(scene, controller);
    }

    private static URL getFxmlResourcePath(FXMLResourceType fxmlResource, Logger logger) {
        loggerHelper(logger, "Loading FXML resource: \"" + fxmlResource + "\"");
        return FXMLResourceLoader.class.getResource(fxmlResource.toString());
    }
    private static void loggerHelper(Logger logger, String logMessage, Exception e) { logger.atFatal().log(logMessage, e);}
    private static void loggerHelper(Logger logger, String logMessage) { logger.atLevel(FXMLResourceLoader.LOAD_MESSAGE_LOG_LEVEL).log(logMessage); }
}
