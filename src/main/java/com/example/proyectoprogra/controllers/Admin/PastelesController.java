package com.example.proyectoprogra.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PastelesController {
    private void cambiarVista(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.setMaximized(true);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void historial(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/historial-admin.fxml", e);}

    @FXML
    void inforPasteles(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/pasteles-admin.fxml",e );}

    @FXML
    void pedidos(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/pedidos-admin.fxml", e);}

    @FXML
    void reservas(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/reservas-admin.fxml", e);}

    @FXML
    void resportes(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/reportes-admin-view.fxml", e);}


    public void informacionUsuario(ActionEvent actionEvent) {
    }
}
