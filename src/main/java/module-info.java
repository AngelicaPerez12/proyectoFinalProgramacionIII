module com.example.proyectoprogra {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proyectoprogra to javafx.fxml;
    opens com.example.proyectoprogra.models to javafx.fxml;
    opens com.example.proyectoprogra.controllers to javafx.fxml;
    exports com.example.proyectoprogra;
    exports com.example.proyectoprogra.models;
}