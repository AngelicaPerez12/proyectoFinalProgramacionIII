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
    exports com.example.proyectoprogra;
    opens com.example.proyectoprogra.models to javafx.fxml;
    exports com.example.proyectoprogra.models;
    opens com.example.proyectoprogra.controllers.Cliente to javafx.fxml;
    exports com.example.proyectoprogra.controllers.Cliente;
    opens com.example.proyectoprogra.controllers.Admin to javafx.fxml;
    exports com.example.proyectoprogra.controllers.Admin;
    opens com.example.proyectoprogra.controllers.Visualizacion to javafx.fxml;
    exports com.example.proyectoprogra.controllers.Visualizacion;

}
