package org.awdevelopment.smithlab.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class FXMLResourceLoader {

    public static Scene load(FXMLResources fxmlResource) {
        Scene scene;
        try {
            scene = new Scene(new FXMLLoader(getFxmlResourcePath(fxmlResource)).load());
        } catch (IOException e) {
            throw new FailedToLoadFXMLException(fxmlResource);
        }
        return scene;
    }

    private static URL getFxmlResourcePath(FXMLResources fxmlResource) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return switch (fxmlResource) {
            case GENERATE_EMPTY_INPUT_SHEET -> classloader.getResource("generate_empty_input_sheet.fxml");
            case GENERATE_OUTPUT_SHEETS -> classloader.getResource("generate_output_sheets.fxml");
        };
    }
}
