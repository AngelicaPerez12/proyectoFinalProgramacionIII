package com.example.proyectoprogra.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RegisterController {

    @FXML
    private TextField campoNombreReg;

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

    private Consumer<String> onSuccessCallback;

    public void setOnSuccess(Consumer<String> callback) {
        this.onSuccessCallback = callback;
    }

    @FXML
    private void registrarUsuario() {
        String nombre = (campoNombreReg != null) ? campoNombreReg.getText() : "";
        String email = (campoEmailReg != null) ? campoEmailReg.getText() : "";
        String pass = (campoContrasenaReg != null) ? campoContrasenaReg.getText() : "";
        String pass2 = (campoContrasenaRegConfirm != null) ? campoContrasenaRegConfirm.getText() : "";

        if (nombre.isBlank() || email.isBlank() || pass.isBlank() || pass2.isBlank()) {
            if (mensajeReg != null) mensajeReg.setText("Por favor complete todos los campos.");
            return;
        }

        if (!pass.equals(pass2)) {
            if (mensajeReg != null) mensajeReg.setText("Las contraseñas no coinciden.");
            return;
        }

        if (mensajeReg != null) mensajeReg.setText("Cuenta creada correctamente. Puedes iniciar sesión.");

        if (onSuccessCallback != null) {
            try {
                onSuccessCallback.accept(email);
            } catch (Exception ignored) {}
        }

        Stage s = (Stage) btnCrearCuentaReg.getScene().getWindow();
        s.close();
    }

    @FXML
    private void cerrar() {
        Stage s = (Stage) btnCancelarReg.getScene().getWindow();
        s.close();
    }
}
