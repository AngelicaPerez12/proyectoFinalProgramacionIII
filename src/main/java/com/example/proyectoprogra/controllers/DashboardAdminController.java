package com.example.proyectoprogra.controllers;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.UsuarioSession;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private VBox panelUsuario;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblCorreoUsuario;

    @FXML
    private Label lblRolUsuario;

    @FXML
    private TextField txtEditarNombre;

    @FXML
    private TextField txtEditarCorreo;

    @FXML
    private Button btnGuardarCambios;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnInfoUsuario;

    @FXML
    public void initialize() {
        panelUsuario.setVisible(false);

        UsuarioSession sesion = UsuarioSession.getInstancia();
        if (sesion != null) {
            lblNombreUsuario.setText("Usuario: " + sesion.getNombre());
            lblCorreoUsuario.setText("Correo: " + sesion.getCorreo());
            lblRolUsuario.setText("Rol: " + sesion.getRol());

            txtEditarNombre.setText(sesion.getNombre());
            txtEditarCorreo.setText(sesion.getCorreo());
        }
    }

    @FXML
    public void informacionUsuario() {

        if (panelUsuario.isVisible()) {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), panelUsuario);
            tt.setFromY(0);
            tt.setToY(-panelUsuario.getHeight());
            tt.setOnFinished(e -> {
                panelUsuario.setVisible(false);
                panelUsuario.setManaged(false);
            });
            tt.play();
        } else {
            UsuarioSession usuario = UsuarioSession.getInstancia();
            lblNombreUsuario.setText("Nombre: " + usuario.getNombre());
            lblCorreoUsuario.setText("Correo: " + usuario.getCorreo());
            lblRolUsuario.setText("Rol: " + usuario.getRol());

            double x = btnInfoUsuario.localToScene(0, 0).getX();
            double y = btnInfoUsuario.localToScene(0, btnInfoUsuario.getHeight()).getY();

            AnchorPane root = (AnchorPane) panelUsuario.getParent();
            javafx.geometry.Point2D pos = root.sceneToLocal(x, y);

            panelUsuario.setLayoutY(pos.getY());

            double maxX = root.getWidth() - panelUsuario.getWidth() - 10; // 10px de margen
            panelUsuario.setLayoutX(Math.min(pos.getX(), maxX));

            panelUsuario.setVisible(true);
            panelUsuario.setManaged(true);
            panelUsuario.setTranslateY(-panelUsuario.getHeight());

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), panelUsuario);
            tt.setFromY(-panelUsuario.getHeight());
            tt.setToY(0);
            tt.play();
        }
    }

    @FXML
    public void guardarCambios() {
        UsuarioSession sesion = UsuarioSession.getInstancia();
        if (sesion == null) return;

        String nuevoNombre = txtEditarNombre.getText();
        String nuevoCorreo = txtEditarCorreo.getText();

        try (Connection con = ConexionDB.getConection()) {
            String sql = "UPDATE tbl_usuarios SET nombre = ?, correo = ? WHERE id_usuario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevoCorreo);
            ps.setInt(3, sesion.getIdUsuario());

            int filas = ps.executeUpdate();
            if (filas > 0) {

                sesion.iniciarSesion(sesion.getIdUsuario(), nuevoNombre, nuevoCorreo, sesion.getRol());

                lblNombreUsuario.setText("Usuario: " + nuevoNombre);
                lblCorreoUsuario.setText("Correo: " + nuevoCorreo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion() {
        UsuarioSession.getInstancia().cerrarSesion();

        System.out.println("Sesión cerrada. Redirigir a login...");
    }

    @FXML
    void historial(ActionEvent event) {

    }

    @FXML
    void inforPasteles(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/pasteles-admin.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.setMaximized(true);

            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
