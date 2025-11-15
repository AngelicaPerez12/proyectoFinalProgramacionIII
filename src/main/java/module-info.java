module com.example.proyectoprogra {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proyectoprogra to javafx.fxml;
    exports com.example.proyectoprogra;
}