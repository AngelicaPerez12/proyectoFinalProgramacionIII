module com.example.proyectoprogra {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires jakarta.mail;
    requires java.sql;

    opens com.example.proyectoprogra to javafx.fxml;
    opens com.example.proyectoprogra.models to javafx.fxml;
    opens com.example.proyectoprogra.controllers to javafx.fxml;


    exports com.example.proyectoprogra;
    exports com.example.proyectoprogra.models;
    exports com.example.proyectoprogra.controllers;
}
