package com.example.proyectoprogra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sweet Harmony");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
