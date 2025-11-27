package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.utils.WindowUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane scrollPane;
    @FXML
    public void initialize() {
        // Activa scroll siempre vertical
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Activa scroll siempre horizontal si quieres
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        if (fondo != null && rootPane != null) {
            fondo.fitWidthProperty().bind(rootPane.widthProperty());
            fondo.fitHeightProperty().bind(rootPane.heightProperty());
            fondo.setPreserveRatio(false);
        }
    }

    // Abrir Registro
    public void abrirRegistro(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/register-view.fxml",
                "Registrarse",
                null);
    }

    // Abrir Login
    public void entrarallogin(ActionEvent event) {
        cambiarVista(event,
                "/com/example/proyectoprogra/Visualizacion/login-view.fxml",
                "Iniciar Sesión",
                "/com/example/proyectoprogra/Styles/login.css");
    }

    // Cerrar ventana
    public void CerrarSesion(ActionEvent event) {
        Stage stage = getStage(event);
        if (stage != null) stage.close();
    }

    /**
     * Cambia la vista dentro del mismo Stage, sin ScrollPane
     */
    private void cambiarVista(ActionEvent event, String fxmlPath, String titulo, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Aplicar CSS si se especifica
            if (cssPath != null) {
                URL cssUrl = getClass().getResource(cssPath);
                if (cssUrl != null) root.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = getStage(event);
            if (stage != null) {
                stage.getScene().setRoot(root);
                stage.setTitle(titulo);
                stage.show();
            } else {
                Stage newStage = new Stage();
                newStage.setScene(new javafx.scene.Scene(root));
                newStage.setTitle(titulo);
                newStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error cargando: " + fxmlPath);
        }
    }

    /**
     * Obtiene el Stage actual a partir del evento o del rootPane
     */
    private Stage getStage(ActionEvent event) {
        if (event != null && event.getSource() instanceof Node) {
            return (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else if (rootPane != null && rootPane.getScene() != null) {
            return (Stage) rootPane.getScene().getWindow();
        }
        return null;
    }
}
