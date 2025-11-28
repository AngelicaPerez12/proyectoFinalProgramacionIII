package com.example.proyectoprogra.controllers.Cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardClienteController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private AnchorPane inicioContent;

    // 📌 Cargar vistas dentro del área dinámica
    private void cargarVista(String ruta) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(ruta));

            contentArea.getChildren().clear();

            AnchorPane.setTopAnchor(vista, 0.0);
            AnchorPane.setBottomAnchor(vista, 0.0);
            AnchorPane.setLeftAnchor(vista, 0.0);
            AnchorPane.setRightAnchor(vista, 0.0);

            contentArea.getChildren().add(vista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------- BOTONES -------------------------

    @FXML
    void hacerpedido(ActionEvent event) {
        cargarVista("/com/example/proyectoprogra/Cliente/pedido-cliente.fxml");
    }

    @FXML
    void historial(ActionEvent event) {
        cargarVista("/com/example/proyectoprogra/Cliente/historial-cliente.fxml");
    }

    @FXML
    void perfil(ActionEvent event) {
        cargarVista("/com/example/proyectoprogra/Cliente/perfil-cliente.fxml");
    }

    @FXML
    void vercatalogo(ActionEvent event) {
        cargarVista("/com/example/proyectoprogra/Cliente/catalogo-cliente.fxml");
    }

    @FXML
    private void regresarDashboard(ActionEvent event) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(inicioContent);
    }
}
