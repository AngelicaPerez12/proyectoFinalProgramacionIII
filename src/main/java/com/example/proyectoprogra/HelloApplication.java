package com.example.proyectoprogra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = HelloApplication.class
                    .getResource("/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml");
            if (fxmlUrl == null) {
                System.err.println("No se encontró Bienvenida.fxml como recurso.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);


            if (root.getStylesheets() != null && !root.getStylesheets().isEmpty()) {
                scene.getStylesheets().addAll(root.getStylesheets());
            }

            stage.setTitle("Sweet Harmony - Pastelería Premium");
            stage.setScene(scene);
            stage.setWidth(1000);
            stage.setHeight(700);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la aplicación: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
