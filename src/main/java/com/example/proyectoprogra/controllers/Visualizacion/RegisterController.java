package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.PasswordUtils;
import com.example.proyectoprogra.utils.WindowUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class RegisterController {

    @FXML private TextField campoNombreReg;
    @FXML private TextField campoApellidoReg;
    @FXML private TextField campoEmailReg;
    @FXML private PasswordField campoContrasenaReg;
    @FXML private PasswordField campoContrasenaRegConfirm;
    @FXML private Button btnCrearCuentaReg;
    @FXML private Button btnCancelarReg;
    @FXML private Label mensajeReg;

    private Consumer<String> onSuccessCallback;

    public void setOnSuccess(Consumer<String> callback) {
        this.onSuccessCallback = callback;
    }

    @FXML
    public void initialize() {
        // Aplicar CSS al Scene cuando esté disponible
        String cssPath = "/com/example/proyectoprogra/css/register.css";
        if (btnCrearCuentaReg != null) {
            btnCrearCuentaReg.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    URL cssUrl = getClass().getResource(cssPath);
                    if (cssUrl != null) newScene.getStylesheets().add(cssUrl.toExternalForm());
                }
            });
        }
    }

    @FXML
    private void registrarUsuario() {
        String nombre = campoNombreReg.getText().trim();
        String apellido = campoApellidoReg.getText().trim();
        String email = campoEmailReg.getText().trim();
        String pass = campoContrasenaReg.getText().trim();
        String pass2 = campoContrasenaRegConfirm.getText().trim();

        if (nombre.isBlank() || email.isBlank() || pass.isBlank() || pass2.isBlank()) {
            mensajeReg.setText("Por favor complete todos los campos.");
            return;
        }

        if (!pass.equals(pass2)) {
            mensajeReg.setText("Las contraseñas no coinciden.");
            return;
        }

        try (Connection conection = ConexionDB.getConection()) {
            String sql = "INSERT INTO tbl_usuarios (nombre, apellido, correo, contraseña, id_rol) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conection.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, PasswordUtils.hashPassword(pass));
            ps.setInt(5, 2);

            int filas = ps.executeUpdate();
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

        } catch (SQLException e) {
            mensajeReg.setText("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrar() {
        cambiarVista("/com/example/proyectoprogra/Visualizacion/login-view.fxml",
                "Iniciar Sesión",
                null,
                btnCancelarReg); // usamos el botón para obtener el Stage
    }

    /**
     * Método centralizado para cambiar vistas dentro del mismo Stage
     * sin ScrollPane ni forzar maximizado
     */
    private void cambiarVista(String fxmlPath, String titulo, String cssPath, Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (cssPath != null) {
                root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mensajeReg.setText("Error al cargar la vista: " + fxmlPath);
        }
    }
}
