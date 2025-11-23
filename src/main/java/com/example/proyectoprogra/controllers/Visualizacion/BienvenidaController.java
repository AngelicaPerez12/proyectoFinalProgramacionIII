package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BienvenidaController {
    public void abrirRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/register-view.fxml")
            );
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.setMaximized(true);
            newStage.setTitle("Registrarse");
            newStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void entrarallogin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Visualizacion/login-view.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            URL cssUrl = HelloApplication.class.getResource("/com/example/proyectoprogra/Styles/login.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            newStage.setMaximized(true);

            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
