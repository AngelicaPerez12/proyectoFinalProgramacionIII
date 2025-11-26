package com.example.proyectoprogra;

import com.example.proyectoprogra.utils.WindowUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml"));
            Parent root = loader.load();

            // Usar WindowUtils para configurar la ventana automáticamente
            // Esto asegura que SIEMPRE se abra maximizada y responsive
            WindowUtils.setupAndShowStage(primaryStage, root, "Sweet Harmony");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
