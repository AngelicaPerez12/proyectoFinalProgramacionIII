package com.example.proyectoprogra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.proyectoprogra.controllers.Visualizacion.LoginController;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {

            URL fxmlUrl = HelloApplication.class.getResource("/com/example/proyectoprogra/Visualizacion/login-view.fxml");
            if (fxmlUrl == null) {
                System.err.println("No se encontró login-view.fxml como recurso.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load());


            URL cssUrl = HelloApplication.class.getResource("/com/example/proyectoprogra/Styles/login.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setTitle("Login - Pastelería");
            stage.setScene(scene);


            Object controller = loader.getController();
            if (controller instanceof LoginController) {
                try {
                    ((LoginController) controller).configurarStage(stage);
                } catch (Exception ignored) { }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
