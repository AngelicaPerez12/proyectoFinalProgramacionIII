package com.example.proyectoprogra.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservasAdminController {
    public void informacionUsuario(ActionEvent actionEvent) {
    }

    public void cambiarVista(String fxml, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void historial(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/historial-admin.fxml", e);}

    @FXML
    void inforPasteles(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/pasteles-admin.fxml",e );}

    @FXML
    void pedidos(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/pedidos-admin.fxml", e);}

    @FXML
    void reservas(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/reservas-admin.fxml", e);}

    @FXML
    void resportes(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/reportes.fxml", e);}

}
