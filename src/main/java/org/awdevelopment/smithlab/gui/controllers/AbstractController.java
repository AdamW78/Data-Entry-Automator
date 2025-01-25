package org.awdevelopment.smithlab.gui.controllers;

import org.apache.logging.log4j.Logger;

public class AbstractController {

    private Logger logger;

    public AbstractController() {}

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
