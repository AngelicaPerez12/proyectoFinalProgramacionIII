package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.models.UsuarioSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.proyectoprogra.models.PasswordUtils;
import com.example.proyectoprogra.ConexionDB.ConexionDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField campoUsuario;
    @FXML
    private PasswordField campoContrasena;
    @FXML
    private Label etiquetaMensaje;
    @FXML
    private Button btnEntrar;
    @FXML
    private Hyperlink forgotLink;
    @FXML
    private Hyperlink openRegisterLink;

    @FXML
    public void initialize() {
    }

    @FXML
    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String contrasenaIngresada = campoContrasena.getText();

        if (usuario.isBlank() || contrasenaIngresada.isBlank()) {
            etiquetaMensaje.setText("Complete todos los campos.");
            return;
        }

        try (Connection conn = ConexionDB.getConection()) {
            String sql = "SELECT id_usuario, nombre, correo, id_rol, contraseña FROM tbl_usuarios WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("contraseña");
                if (!PasswordUtils.verifyPassword(contrasenaIngresada, hashGuardado)) {
                    etiquetaMensaje.setText("Usuario o contraseña incorrectos.");
                    return;
                }

                int idUsuario = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                int idRol = rs.getInt("id_rol");
                String rol = idRol == 1 ? "Administrador" : "Cliente";

                UsuarioSession.getInstancia().iniciarSesion(idUsuario, nombre, correo, rol);

                Stage stage = (Stage) btnEntrar.getScene().getWindow();
                stage.close();

                abrirVentana(rol.equalsIgnoreCase("Administrador")
                        ? "/com/example/proyectoprogra/Admin/dashboard-admin.fxml"
                        : "/com/example/proyectoprogra/Cliente/dashboard-cliente.fxml");

            } else {
                etiquetaMensaje.setText("Usuario o contraseña incorrectos.");
            }

        } catch (SQLException e) {
            etiquetaMensaje.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/register-view.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();
            if (ctrl instanceof RegisterController rc) {
                rc.setOnSuccess(email -> Platform.runLater(() -> {
                    try {
                        FXMLLoader loginLoader = new FXMLLoader(
                                getClass().getResource("/com/example/proyectoprogra/Visualizacion/login-view.fxml"));
                        Parent loginRoot = loginLoader.load();
                        Object possibleCtrl = loginLoader.getController();
                        if (possibleCtrl instanceof LoginController newLogin) {
                            newLogin.preFillEmail(email);
                        }

                        Stage stage = (Stage) campoUsuario.getScene().getWindow();
                        Scene scene = new Scene(loginRoot);
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));
            }

            Stage stage = (Stage) campoUsuario.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRecuperacionDePin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/recuperacion-pin-view.fxml"));
            Parent root = loader.load();
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Recuperación de PIN");
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentana(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void CerrarSesionLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1000);
            stage.setHeight(700);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preFillEmail(String email) {
        if (email == null) return;
        if (campoUsuario != null) {
            campoUsuario.setText(email);
            campoUsuario.requestFocus();
        }
    }
}
