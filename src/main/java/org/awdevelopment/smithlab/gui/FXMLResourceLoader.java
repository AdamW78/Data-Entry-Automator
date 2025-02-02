package org.awdevelopment.smithlab.gui;

import org.apache.logging.log4j.Logger;
import org.awdevelopment.smithlab.gui.controllers.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;

public class FXMLResourceLoader {

    public static FXMLResource load(FXMLResourceType fxmlResource, Logger logger) {
        Scene scene;
        AbstractController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getFxmlResourcePath(fxmlResource));
            scene = new Scene(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            String logMessage = "Failed to load FXML resource: " + fxmlResource;
            logger.atFatal().log(logMessage);
            logger.atFatal().log(e);
            throw new FailedToLoadFXMLException(fxmlResource);
        }
        return new FXMLResource(scene, controller);
    }

    private static URL getFxmlResourcePath(FXMLResourceType fxmlResource) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        System.out.println("FXMLResourceLoader.class.getResource(\"/fxml/application.fxml\"): " + FXMLResourceLoader.class.getResource("/fxml/application.fxml"));
        // System.out.println("getClass().getResource(\"/fxml/application.fxml\"): " + getClass().getResource("/fxml/application.fxml"));
        System.out.println("classloader.getResource(\"/fxml/application.fxml\"): " + classloader.getResource("/fxml/application.fxml"));
        return switch (fxmlResource) {
            case APPLICATION -> FXMLResourceLoader.class.getResource("/fxml/application.fxml");
            case CONDITIONS -> classloader.getResource("conditions.fxml");
            case STRAINS -> classloader.getResource("strains.fxml");
            case TIMEPOINTS -> classloader.getResource("timepoints.fxml");
        };
    }
}
