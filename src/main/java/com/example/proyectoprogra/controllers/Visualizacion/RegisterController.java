package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.proyectoprogra.models.PasswordUtils;

import java.util.function.Consumer;
import javafx.scene.layout.StackPane;

public class RegisterController {

    @FXML
    private TextField campoNombreReg;

    @FXML
    private TextField campoApellidoReg;

    @FXML
    private TextField campoEmailReg;

    @FXML
    private PasswordField campoContrasenaReg;

    @FXML
    private PasswordField campoContrasenaRegConfirm;

    @FXML
    private Button btnCrearCuentaReg;

    @FXML
    private Button btnCancelarReg;

    @FXML
    private Label mensajeReg;

    @FXML
    private StackPane rootPane; // añadido para aplicar CSS programáticamente


    private Consumer<String> onSuccessCallback;

    public void setOnSuccess(Consumer<String> callback) {
        this.onSuccessCallback = callback;
    }

    @FXML
    public void initialize() {
        // Aplicar stylesheet programáticamente cuando la Scene esté disponible
        String cssPath = "/com/example/proyectoprogra/Styles/register.css";
        if (rootPane != null) {
            rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    URL cssUrl = getClass().getResource(cssPath);
                    if (cssUrl != null) newScene.getStylesheets().add(cssUrl.toExternalForm());
                }
            });
            if (rootPane.getScene() != null) {
                URL cssUrl = getClass().getResource(cssPath);
                if (cssUrl != null) rootPane.getScene().getStylesheets().add(cssUrl.toExternalForm());
            }
        }
    }

    @FXML
    private void registrarUsuario() {
        String nombre = campoNombreReg.getText();
        String apellido = campoApellidoReg.getText();
        String email = campoEmailReg.getText();
        String pass = campoContrasenaReg.getText();
        String pass2 = campoContrasenaRegConfirm.getText();

        if (nombre.isBlank() || email.isBlank() || pass.isBlank() || pass2.isBlank()) {
            mensajeReg.setText("Por favor complete todos los campos.");
            return;
        }

        if (!pass.equals(pass2)) {
            mensajeReg.setText("Las contraseñas no coinciden.");
            return;
        }

        try (Connection conection = ConexionDB.getConection()) {

            String sql = "INSERT INTO tbl_usuarios (nombre, apellido, correo, contraseña, id_rol) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conection.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, PasswordUtils.hashPassword(pass));
            ps.setInt(5, 2);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                mensajeReg.setText("Cuenta creada correctamente. Puedes iniciar sesión.");
                if (filas > 0) {
                    mensajeReg.setText("Cuenta creada correctamente. Puedes iniciar sesión.");
                    campoNombreReg.clear();
                    campoApellidoReg.clear();
                    campoEmailReg.clear();
                    campoContrasenaReg.clear();
                    campoContrasenaRegConfirm.clear();

                    if (onSuccessCallback != null) {
                        onSuccessCallback.accept(email);
                    }

                } else {
                    mensajeReg.setText("Error al crear la cuenta.");
                }

                if (onSuccessCallback != null) {
                    onSuccessCallback.accept(email);
                }

            } else {
                mensajeReg.setText("Error al crear la cuenta.");
            }

        } catch (SQLException e) {
            mensajeReg.setText("Error de conexión: " + e.getMessage());
        }
    }

    @FXML
    private void cerrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Visualizacion/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCancelarReg.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setFullScreen(false);
            stage.setResizable(true);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mensajeReg.setText("Error al abrir la pantalla de inicio de sesión.");
        }
    }


}