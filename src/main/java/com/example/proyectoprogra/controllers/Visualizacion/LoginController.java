package com.example.proyectoprogra.controllers.Visualizacion;

import com.example.proyectoprogra.models.UsuarioSession;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.proyectoprogra.models.PasswordUtils;
import com.example.proyectoprogra.ConexionDB.ConexionDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoContrasena;
    @FXML private TextField campoContrasenaVisible;
    @FXML private Label etiquetaMensaje;
    @FXML private VBox tarjeta;
    @FXML private VBox contenedorCentral;
    @FXML private Label tituloBienvenido;
    @FXML private Label subtituloBienvenido;
    @FXML private Label tituloCard;
    @FXML private Label subtituloCard;
    @FXML private Label etiquetaUsuario;
    @FXML private Label etiquetaContrasena;
    @FXML private VBox loginPanel;
    @FXML private ToggleButton showPassword;
    @FXML private CheckBox rememberCheck;
    @FXML private Hyperlink forgotLink;
    @FXML private Button btnEntrar;
    @FXML private Hyperlink openRegisterLink;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (campoContrasenaVisible != null && campoContrasena != null) {
                campoContrasena.textProperty().addListener((obs, oldV, newV) -> {
                    if (!campoContrasenaVisible.isFocused()) campoContrasenaVisible.setText(newV);
                });
                campoContrasenaVisible.textProperty().addListener((obs, oldV, newV) -> {
                    if (!campoContrasena.isFocused()) campoContrasena.setText(newV);
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
        });
    }

    public void configurarStage(Stage stage) {
        if (tarjeta != null) {
            tarjeta.prefWidthProperty().bind(Bindings.createDoubleBinding(
                    () -> Math.max(300, stage.getScene().getWidth() * 0.35),
                    stage.widthProperty(), stage.heightProperty()));

            campoUsuario.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
            campoContrasena.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
            if (campoContrasenaVisible != null)
                campoContrasenaVisible.prefWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));

            tituloBienvenido.maxWidthProperty().bind(tarjeta.prefWidthProperty());
            subtituloBienvenido.maxWidthProperty().bind(tarjeta.prefWidthProperty());
            tituloCard.maxWidthProperty().bind(tarjeta.prefWidthProperty());
            subtituloCard.maxWidthProperty().bind(tarjeta.prefWidthProperty());
            etiquetaUsuario.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
            etiquetaContrasena.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));
            etiquetaMensaje.maxWidthProperty().bind(tarjeta.prefWidthProperty().subtract(40));

            contenedorCentral.setAlignment(Pos.CENTER);
            contenedorCentral.setFillWidth(false);
            contenedorCentral.setTranslateX(0);

            tarjeta.minWidthProperty().set(280);
            tarjeta.maxWidthProperty().bind(Bindings.createDoubleBinding(
                    () -> Math.min(stage.getScene().getWidth() - 80, 600.0), stage.widthProperty()));

            stage.widthProperty().addListener((obs, oldV, newV) -> Platform.runLater(() -> tarjeta.requestLayout()));
            stage.heightProperty().addListener((obs, oldV, newV) -> Platform.runLater(() -> tarjeta.requestLayout()));
        }
    }

    @FXML
    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String contrasenaIngresada = showPassword.isSelected() ? campoContrasenaVisible.getText() : campoContrasena.getText();

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
    private void togglePasswordVisibility() {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Visualizacion/register-view.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();
            if (ctrl instanceof RegisterController rc) {
                rc.setOnSuccess(email -> Platform.runLater(() -> {
                    campoUsuario.setText(email);
                    campoUsuario.requestFocus();
                }));
            }
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRecuperacionDePin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Visualizacion/recuperacion-pin-view.fxml"));
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
                    getClass().getResource("/com/example/proyectoprogra/Visualizacion/bienvenida.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setMaximized(true);

            stage.sizeToScene();
            Platform.runLater(() -> {
                stage.setMaximized(true);
                root.requestLayout();
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
