package com.example.proyectoprogra;

import com.example.proyectoprogra.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Bienvenida.fxml"));
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