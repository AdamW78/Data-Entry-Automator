package org.awdevelopment.smithlab.gui;

import org.apache.logging.log4j.Level;
import org.awdevelopment.smithlab.gui.controllers.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.IOException;
import java.net.URL;

public class FXMLResourceLoader {

    private static final Level LOAD_MESSAGE_LOG_LEVEL = Level.DEBUG;

    public static FXMLResource load(FXMLResourceType fxmlResource, LoggerHelper LOGGER) {
        Scene scene;
        AbstractController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getFxmlResourcePath(fxmlResource, LOGGER));
            scene = new Scene(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            LOGGER.atFatal("Failed to load FXML resource: \"" + fxmlResource+"\"", e);
            throw new FailedToLoadFXMLException(fxmlResource, e);
        }
        return new FXMLResource(scene, controller);
    }

    private static URL getFxmlResourcePath(FXMLResourceType fxmlResource, LoggerHelper LOGGER) {
        LOGGER.atLevel("Loading FXML resource: \"" + fxmlResource + "\"", LOAD_MESSAGE_LOG_LEVEL);
        return FXMLResourceLoader.class.getResource(fxmlResource.toString());
    }
}
