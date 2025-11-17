package com.example.proyectoprogra.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardAdminController {
    @FXML
    private Button btnhistorial;

    @FXML
    private Button btnpastel;

    @FXML
    private Button btnpedidos;

    @FXML
    private Button btnreportes;

    @FXML
    private Button btnreservas;

    @FXML
    private Button usuario;

    @FXML
    void historial(ActionEvent event) {

    }

    @FXML
    void inforPasteles(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/proyectoprogra/pasteles-admin.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/com/example/proyectoprogra/Styles/dashadmin.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void informacionUsuario(ActionEvent event) {

    }

    @FXML
    void pedidos(ActionEvent event) {

    }

    @FXML
    void reservas(ActionEvent event) {

    }

    @FXML
    void resportes(ActionEvent event) {

    }
}
