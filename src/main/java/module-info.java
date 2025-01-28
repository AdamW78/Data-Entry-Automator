module org.awdevelopment.smithlab {
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires java.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.xml;
    exports org.awdevelopment.smithlab;
    exports org.awdevelopment.smithlab.gui;
    exports org.awdevelopment.smithlab.gui.controllers;
    opens org.awdevelopment.smithlab.gui to javafx.fxml, javafx.graphics, javafx.controls;
    opens org.awdevelopment.smithlab.gui.controllers to javafx.fxml, javafx.graphics, javafx.controls;
    opens org.awdevelopment.smithlab to javafx.fxml, javafx.graphics, javafx.controls;
    exports org.awdevelopment.smithlab.args.exceptions;
    exports org.awdevelopment.smithlab.config;
    exports org.awdevelopment.smithlab.io.output.formats;
    exports org.awdevelopment.smithlab.args;
}