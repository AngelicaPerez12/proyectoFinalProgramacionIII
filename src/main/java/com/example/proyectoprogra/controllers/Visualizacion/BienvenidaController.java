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
    private ImageView fondo; // Fondo del FXML

    @FXML
    private StackPane rootPane; // Contenedor principal

    /**
     * Ajusta el fondo para que siempre ocupe TODA la pantalla
     */
    @FXML
    public void initialize() {
        if (fondo != null && rootPane != null) {
            fondo.fitWidthProperty().bind(rootPane.widthProperty());
            fondo.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }

    /**
     * Abre la vista de registro y cierra la actual
     */
    public void abrirRegistro(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/register-view.fxml",
                "Registrarse",
                null
        );
    }

    /**
     * Abre el login y aplica CSS
     */
    public void entrarallogin(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/login-view.fxml",
                "Iniciar Sesión",
                "/com/example/proyectoprogra/Styles/login.css"
        );
    }

    /**
     * Cerrar la ventana actual
     */
    public void CerrarSesion(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();
        currentStage.close();
    }

    // ============================================================
    // MÉTODO AUXILIAR PARA CAMBIO DE ESCENAS
    // ============================================================

    private void cambiarVista(ActionEvent event, String fxmlPath, String titulo, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            if (cssPath != null) {
                URL cssUrl = getClass().getResource(cssPath);
                if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error cargando: " + fxmlPath);
        }
    }
}
