module org.awdevelopment.smithlab {
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.apache.commons.compress;
    exports org.awdevelopment.smithlab;
    exports org.awdevelopment.smithlab.gui;
    exports org.awdevelopment.smithlab.gui.controllers;
    exports org.awdevelopment.smithlab.logging;
    exports org.awdevelopment.smithlab.args.exceptions;
    exports org.awdevelopment.smithlab.config;
    exports org.awdevelopment.smithlab.io.output.formats;
    exports org.awdevelopment.smithlab.args;
    exports org.awdevelopment.smithlab.data;
    opens org.awdevelopment.smithlab.gui to javafx.fxml, javafx.graphics, javafx.controls;
    opens org.awdevelopment.smithlab.gui.controllers to javafx.fxml, javafx.graphics, javafx.controls;
    opens org.awdevelopment.smithlab to javafx.fxml, javafx.graphics, javafx.controls;
    exports org.awdevelopment.smithlab.config.exceptions;
    opens org.awdevelopment.smithlab.config to javafx.controls, javafx.fxml, javafx.graphics;
    exports org.awdevelopment.smithlab.gui.controllers.main;
    opens org.awdevelopment.smithlab.gui.controllers.main to javafx.controls, javafx.fxml, javafx.graphics;
}