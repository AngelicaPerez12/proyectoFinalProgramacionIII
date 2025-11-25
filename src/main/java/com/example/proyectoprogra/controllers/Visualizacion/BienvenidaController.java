package com.example.proyectoprogra.controllers.Visualizacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BienvenidaController {

    @FXML
    private ImageView fondo;

    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        if (fondo != null && rootPane != null) {
            fondo.fitWidthProperty().bind(rootPane.widthProperty());
            fondo.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }

    public void abrirRegistro(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/register-view.fxml",
                "Registrarse",
                null
        );
    }

    public void entrarallogin(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/login-view.fxml",
                "Iniciar Sesión",
                "/com/example/proyectoprogra/Styles/login.css"
        );
    }

    public void CerrarSesion(ActionEvent event) {
        Stage currentStage = null;
        if (event != null && event.getSource() instanceof Node) {
            currentStage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
        } else if (rootPane != null && rootPane.getScene() != null) {
            currentStage = (Stage) rootPane.getScene().getWindow();
        }
        if (currentStage != null) currentStage.close();
    }

    private void cambiarVista(ActionEvent event, String fxmlPath, String titulo, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            if (cssPath != null) {
                URL cssUrl = getClass().getResource(cssPath);
                if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage currentStage = null;
            if (event != null && event.getSource() instanceof Node) {
                currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else if (rootPane != null && rootPane.getScene() != null) {
                currentStage = (Stage) rootPane.getScene().getWindow();
            }

            if (currentStage != null) {
                currentStage.setTitle(titulo);
                currentStage.setScene(scene);
                currentStage.setMaximized(true);
                currentStage.show();
            } else {
                Stage stage = new Stage();
                stage.setTitle(titulo);
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error cargando: " + fxmlPath);
        }
    }
}
