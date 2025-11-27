package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.models.UsuarioSession;
import com.example.proyectoprogra.models.PasswordUtils;
import com.example.proyectoprogra.ConexionDB.ConexionDB;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoContrasena;
    @FXML private Label etiquetaMensaje;
    @FXML private Button btnEntrar;
    @FXML private Hyperlink forgotLink;
    @FXML private Hyperlink openRegisterLink;

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
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

                // Abrir dashboard según rol
                abrirVentana(
                        rol.equalsIgnoreCase("Administrador")
                                ? "/com/example/proyectoprogra/Admin/dashboard-admin.fxml"
                                : "/com/example/proyectoprogra/Cliente/dashboard-cliente.fxml",
                        "Dashboard"
                );

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
        cambiarVista(
                "/com/example/proyectoprogra/Visualizacion/register-view.fxml",
                "Registrarse",
                "/com/example/proyectoprogra/Styles/bakery-theme.css"
        );
    }

    @FXML
    private void abrirRecuperacionDePin() {
        cambiarVista(
                "/com/example/proyectoprogra/Visualizacion/recuperacion-pin-view.fxml",
                "Recuperar PIN",
                null
        );
    }

    @FXML
    private void CerrarSesionLogin() {
        cambiarVista(
                "/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml",
                "Sweet Harmony",
                null
        );
    }


    private void cambiarVista(String fxmlPath, String titulo, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (cssPath != null) {
                root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            Stage stage = (Stage) campoUsuario.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error cargando: " + fxmlPath);
        }
    }

    // Abrir un Stage nuevo (dashboard)
    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle(titulo);
            stage.setMaximized(true); // Ahora sí se abre maximizado
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error cargando: " + fxmlPath);
        }
    }

    // Rellenar email si viene de registro o recuperación
    public void preFillEmail(String email) {
        if (email != null && campoUsuario != null) {
            campoUsuario.setText(email);
            campoUsuario.requestFocus();
        }
    }
}
