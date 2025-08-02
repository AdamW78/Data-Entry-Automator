package org.awdevelopment.smithlab.gui.controllers;

import org.awdevelopment.smithlab.config.EmptyInputSheetConfig;
import org.awdevelopment.smithlab.gui.controllers.main.MainApplicationController;

public abstract class AbstractLabelController extends AbstractController {

    private EmptyInputSheetConfig config;
    private MainApplicationController mainController;

    public AbstractLabelController() { super(); }

    public void setConfig(EmptyInputSheetConfig config) {
        this.config = config;
    }

    public void setMainController(MainApplicationController mainController) { this.mainController = mainController; }

    public EmptyInputSheetConfig config() {
        return config;
    }

    public MainApplicationController getMainController() { return mainController; }

}
