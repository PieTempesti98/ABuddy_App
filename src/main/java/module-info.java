module it.unipi.dii.dmml.abuddy.abuddy_application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires weka.stable;
    requires java.sql;
    // requires KValid;

    opens it.unipi.dii.dmml.abuddy.abuddy_application to javafx.fxml;
    exports it.unipi.dii.dmml.abuddy.abuddy_application;
    exports it.unipi.dii.dmml.abuddy.abuddy_application.controller;
    opens it.unipi.dii.dmml.abuddy.abuddy_application.controller to javafx.fxml;
    exports it.unipi.dii.dmml.abuddy.abuddy_application.entities;
    opens it.unipi.dii.dmml.abuddy.abuddy_application.entities to javafx.fxml;
}