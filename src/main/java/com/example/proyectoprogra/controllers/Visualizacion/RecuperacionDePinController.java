package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.EmailSender;
import com.example.proyectoprogra.models.PasswordUtils;
import com.example.proyectoprogra.utils.WindowUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class RecuperacionDePinController {

    @FXML private TextField campoEmailRec, campoCodigoRec;
    @FXML private PasswordField campoPinNuevo, campoPinConfirm;
    @FXML private Label mensajeRec;

    private String codigoGenerado;

    @FXML
    private void enviarCodigo() {
        String email = campoEmailRec.getText().trim();
        if (email.isEmpty()) {
            mensajeRec.setText("Ingresa tu correo.");
            return;
        }

        codigoGenerado = generarCodigo(6);
        EmailSender.enviarCodigo(email, codigoGenerado);
        mensajeRec.setText("Se ha enviado un código a tu correo.");
    }

    @FXML
    private void recuperarPin() {
        String codigoIngresado = campoCodigoRec.getText().trim();
        String nuevoPin = campoPinNuevo.getText().trim();
        String confirmarPin = campoPinConfirm.getText().trim();
        String email = campoEmailRec.getText().trim();

        if (codigoIngresado.isEmpty() || nuevoPin.isEmpty() || confirmarPin.isEmpty()) {
            mensajeRec.setText("Completa todos los campos.");
            return;
        }

        if (!codigoIngresado.equals(codigoGenerado)) {
            mensajeRec.setText("Código incorrecto.");
            return;
        }

        if (!nuevoPin.equals(confirmarPin)) {
            mensajeRec.setText("Los PIN no coinciden.");
            return;
        }

        String hashedPin = PasswordUtils.hashPassword(nuevoPin);
        actualizarPinEnBD(email, hashedPin);
    }

    private void actualizarPinEnBD(String email, String hashedPin) {
        String sql = "UPDATE tbl_usuarios SET contraseña = ? WHERE correo = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hashedPin);
            ps.setString(2, email);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                mensajeRec.setText("PIN actualizado correctamente.");
                campoEmailRec.clear();
                campoCodigoRec.clear();
                campoPinNuevo.clear();
                campoPinConfirm.clear();
            } else {
                mensajeRec.setText("No se encontró el usuario con ese correo.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mensajeRec.setText("Error al actualizar el PIN.");
        }
    }

    private String generarCodigo(int longitud) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder codigo = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < longitud; i++) {
            codigo.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return codigo.toString();
    }

    @FXML
    private void cerrar(ActionEvent event) {
        cambiarVista("/com/example/proyectoprogra/Visualizacion/login-view.fxml", "Iniciar Sesión", null, event);
    }

    @FXML
    private void cerrarVentanaPIN(ActionEvent event) {
        cerrar(event);
    }

    /**
     * Método centralizado para cambiar vistas dentro del mismo Stage
     * sin ScrollPane ni maximizar
     */
    private void cambiarVista(String fxmlPath, String titulo, String cssPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (cssPath != null) {
                root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            Stage stage = (event != null && event.getSource() instanceof Node)
                    ? (Stage) ((Node) event.getSource()).getScene().getWindow()
                    : (Stage) campoEmailRec.getScene().getWindow();

            // Reemplazamos el root actual sin forzar scroll ni maximizar
            stage.getScene().setRoot(root);
            stage.setTitle(titulo);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mensajeRec.setText("Error al cargar la vista: " + fxmlPath);
        }
    }
}
