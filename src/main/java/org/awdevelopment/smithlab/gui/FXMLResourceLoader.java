package org.awdevelopment.smithlab.gui;

import org.awdevelopment.smithlab.gui.controllers.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;

public class FXMLResourceLoader {

    public static FXMLResource load(FXMLResourceType fxmlResource) {
        Scene scene;
        AbstractController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getFxmlResourcePath(fxmlResource));
            scene = new Scene(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            throw new FailedToLoadFXMLException(fxmlResource);
        }
        return new FXMLResource(scene, controller);
    }

    private static URL getFxmlResourcePath(FXMLResourceType fxmlResource) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return switch (fxmlResource) {
            case APPLICATION -> classloader.getResource("fxml/application.fxml");
            case CONDITIONS -> classloader.getResource("conditions.fxml");
            case STRAINS -> classloader.getResource("strains.fxml");
            case TIMEPOINTS -> classloader.getResource("timepoints.fxml");
        };
    }
}
