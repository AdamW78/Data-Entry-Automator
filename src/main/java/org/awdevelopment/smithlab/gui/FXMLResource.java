package org.awdevelopment.smithlab.gui;

import javafx.scene.Scene;
import org.awdevelopment.smithlab.gui.controllers.AbstractController;

public record FXMLResource(Scene scene, AbstractController controller) {}