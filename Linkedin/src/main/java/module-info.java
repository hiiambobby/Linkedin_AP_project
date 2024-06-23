module Linkedin {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.sql;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.httpserver;

    requires jjwt.api;
    requires org.json;

    opens com.backend.client.controllers to javafx.fxml;
    exports com.backend.client.controllers;

    // Export your package to javafx.graphics
    exports com.backend.client to javafx.graphics;
}