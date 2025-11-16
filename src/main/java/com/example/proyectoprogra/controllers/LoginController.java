package com.example.proyectoprogra.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.proyectoprogra.models.Usuario;

import java.io.IOException;
import java.net.URL;

public class LoginController {
    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private TextField campoContrasenaVisible;

    @FXML
    private Label etiquetaMensaje;

    @FXML
    private VBox tarjeta;

    @FXML
    private VBox contenedorCentral;

    @FXML
    private Label tituloBienvenido;

    @FXML
    private Label subtituloBienvenido;

    @FXML
    private Label tituloCard;

    @FXML
    private Label subtituloCard;

    @FXML
    private Label etiquetaUsuario;

    @FXML
    private Label etiquetaContrasena;

    @FXML
    private VBox loginPanel;

    @FXML
    private ToggleButton showPassword;

    @FXML
    private CheckBox rememberCheck;

    @FXML
    private Hyperlink forgotLink;

    @FXML
    private Button btnEntrar;

    @FXML
    private Hyperlink openRegisterLink;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                System.out.println("[DEBUG] btnEntrar styleClass: " + btnEntrar.getStyleClass());
            } catch (Exception ignored) {}
            try {
                Scene s = btnEntrar.getScene();
                if (s != null) {
                    System.out.println("[DEBUG] Stylesheets en escena: ");
                    s.getStylesheets().forEach(ss -> System.out.println("  - " + ss));
                }
            } catch (Exception ignored) {}
        });

        if (campoContrasenaVisible != null && campoContrasena != null) {
            campoContrasena.textProperty().addListener((obs, oldV, newV) -> {
                if (campoContrasenaVisible != null && !campoContrasenaVisible.isFocused()) {
                    campoContrasenaVisible.setText(newV);
                }
            });

            campoContrasenaVisible.textProperty().addListener((obs, oldV, newV) -> {
                if (campoContrasena != null && !campoContrasena.isFocused()) {
                    campoContrasena.setText(newV);
                }
            });

            campoContrasenaVisible.setVisible(false);
            campoContrasenaVisible.setManaged(false);
        }

        if (loginPanel != null) {
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
        }

        if (btnEntrar != null) {
            DropShadow hoverShadow = new DropShadow(8, Color.rgb(0,0,0,0.14));
            btnEntrar.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                btnEntrar.setEffect(hoverShadow);
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btnEntrar);
                st.setToX(1.02); st.setToY(1.02); st.playFromStart();
            });
            btnEntrar.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                btnEntrar.setEffect(null);
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btnEntrar);
                st.setToX(1.0); st.setToY(1.0); st.playFromStart();
            });
            btnEntrar.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                btnEntrar.setScaleX(0.995); btnEntrar.setScaleY(0.995);
            });
            btnEntrar.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                btnEntrar.setScaleX(1.0); btnEntrar.setScaleY(1.0);
            });
        }

        if (openRegisterLink != null) {
            openRegisterLink.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(160), openRegisterLink);
                ft.setToValue(0.85); ft.playFromStart();
            });
            openRegisterLink.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                FadeTransition ft = new FadeTransition(Duration.millis(160), openRegisterLink);
                ft.setToValue(1.0); ft.playFromStart();
            });
        }
    }

    public void configurarStage(Stage stage) {
        stage.setMaximized(true);

        tarjeta.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> Math.max(300, stage.getScene().getWidth() * 0.35),
                stage.widthProperty(), stage.heightProperty()));

        campoUsuario.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
        campoContrasena.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
        if (campoContrasenaVisible != null) {
            campoContrasenaVisible.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
        }

        tituloBienvenido.maxWidthProperty().bind(tarjeta.prefWidthProperty());
        subtituloBienvenido.maxWidthProperty().bind(tarjeta.prefWidthProperty());
        tituloCard.maxWidthProperty().bind(tarjeta.prefWidthProperty());
        subtituloCard.maxWidthProperty().bind(tarjeta.prefWidthProperty());
        etiquetaUsuario.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
        etiquetaContrasena.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
        etiquetaMensaje.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));

        if (contenedorCentral != null) {
            contenedorCentral.setAlignment(Pos.CENTER);
            contenedorCentral.setFillWidth(false);
            contenedorCentral.setTranslateX(0);
        }

        tarjeta.minWidthProperty().set(280);
        tarjeta.maxWidthProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(stage.getScene().getWidth() - 80, 600.0), stage.widthProperty()));

        stage.widthProperty().addListener((obs, oldV, newV) -> Platform.runLater(() -> tarjeta.requestLayout()));
        stage.heightProperty().addListener((obs, oldV, newV) -> Platform.runLater(() -> tarjeta.requestLayout()));
    }

    @FXML
    private void iniciarSesion() {
        String usuario = (campoUsuario != null) ? campoUsuario.getText() : "";
        String contrasena = (campoContrasena != null && campoContrasena.isVisible()) ? campoContrasena.getText() :
                (campoContrasenaVisible != null ? campoContrasenaVisible.getText() : "");

        if (usuario == null || usuario.isBlank() || contrasena == null || contrasena.isBlank()) {
            if (etiquetaMensaje != null) etiquetaMensaje.setText("Por favor complete todos los campos.");
            return;
        }

        Usuario u = new Usuario(usuario, contrasena);
        if ("admin".equals(u.getUsuario()) && "admin".equals(u.getContrasena())) {
            if (etiquetaMensaje != null) etiquetaMensaje.setText("Login exitoso");
        } else {
            if (etiquetaMensaje != null) etiquetaMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        if (showPassword == null || campoContrasena == null || campoContrasenaVisible == null) return;

        if (showPassword.isSelected()) {
            campoContrasenaVisible.setText(campoContrasena.getText());
            campoContrasenaVisible.setVisible(true);
            campoContrasenaVisible.setManaged(true);
            campoContrasena.setVisible(false);
            campoContrasena.setManaged(false);
        } else {
            campoContrasena.setText(campoContrasenaVisible.getText());
            campoContrasena.setVisible(true);
            campoContrasena.setManaged(true);
            campoContrasenaVisible.setVisible(false);
            campoContrasenaVisible.setManaged(false);
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/proyectoprogra/register-view.fxml");
            if (fxmlUrl == null) {
                System.err.println("No se encontró register-view.fxml como recurso.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Object ctrl = loader.getController();
            if (ctrl instanceof RegisterController) {
                RegisterController rc = (RegisterController) ctrl;
                rc.setOnSuccess(email -> Platform.runLater(() -> {
                    if (campoUsuario != null) {
                        campoUsuario.setText(email);
                        campoUsuario.requestFocus();
                    }
                }));
            }

            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("/com/example/proyectoprogra/Styles/login.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);

            if (tarjeta != null && tarjeta.getScene() != null && tarjeta.getScene().getWindow() instanceof Stage) {
                dialog.initOwner((Stage) tarjeta.getScene().getWindow());
            }
            dialog.setTitle("Crear cuenta");
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginController setLoginPanel(VBox loginPanel) {
        this.loginPanel = loginPanel;
        return this;
    }
}
