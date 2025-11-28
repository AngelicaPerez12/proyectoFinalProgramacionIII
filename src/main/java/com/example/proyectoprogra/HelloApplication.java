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
        // Archivo FXML de arranque (cambiar aquí si deseas otra vista)
        String fxmlPath = "/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml";

        try {
            URL fxmlUrl = HelloApplication.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("No se encontró Bienvenida.fxml como recurso.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Opción B: No cargar un CSS global desde Java. Confiamos en que cada FXML
            // incluya su propio tema (por ejemplo bakery-theme.css) si lo necesita.

            // Añadir cualquier stylesheet declarado en el FXML (el loader suele poblar root.getStylesheets())
            if (root.getStylesheets() != null && !root.getStylesheets().isEmpty()) {
                scene.getStylesheets().addAll(root.getStylesheets());
            } else {
                // Fallback: buscar un CSS con el mismo nombre que el FXML dentro de /css/
                try {
                    String name = fxmlPath.substring(fxmlPath.lastIndexOf('/') + 1, fxmlPath.lastIndexOf('.'));
                    String cssPath = "/com/example/proyectoprogra/css/" + name.toLowerCase() + ".css";
                    URL cssUrl = HelloApplication.class.getResource(cssPath);
                    if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
                } catch (Exception ignored) {
                    // No hacer nada si falla la detección del fallback
                }
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
