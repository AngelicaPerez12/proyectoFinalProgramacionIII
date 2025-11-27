package com.example.proyectoprogra.controllers.Admin;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.ScrollPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DashboardAdminController {

    @FXML
    private Button btnhistorial;
    @FXML
    private Button btndashboard;
    @FXML
    private Button btnpastel;
    @FXML
    private Button btnpedidos;
    @FXML
    private Button btnreportes;
    @FXML
    private Button btnreservas;
    @FXML
    private Button btnInfoUsuario;
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
    private StackPane contenidoPrincipal;
    @FXML
    private Label InformarcionDeInicio;
    @FXML private javafx.scene.image.ImageView logo;
    @FXML private Button cerrarSesionDashboard;

    @FXML
    public void initialize() {
        // Obtenemos la instancia de la sesión
        UsuarioSession usuario = UsuarioSession.getInstancia();

        // Actualizamos el label con nombre y rol
        if (usuario != null) {
            InformarcionDeInicio.setText("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        } else {
            InformarcionDeInicio.setText("Usuario no iniciado");
        }
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
            double maxX = root.getWidth() - panelUsuario.getWidth() - 10; // margen
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

    private void cambiarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Reemplaza contenido del StackPane central
            contenidoPrincipal.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void historial(ActionEvent e) {
        cambiarVista("/com/example/proyectoprogra/Admin/historial-admin.fxml");
    }

    @FXML
    void inforPasteles(ActionEvent e) {
        cambiarVista("/com/example/proyectoprogra/Admin/pasteles-admin.fxml");
    }

    @FXML
    void pedidos(ActionEvent e) {
        cambiarVista("/com/example/proyectoprogra/Admin/pedidos-admin.fxml");
    }

    @FXML
    void resportes(ActionEvent e) {
        cambiarVista("/com/example/proyectoprogra/Admin/reportes-admin-view.fxml");
    }
    @FXML
    void regresarDashboard(ActionEvent e) {
        contenidoPrincipal.getChildren().clear();
        contenidoPrincipal.getChildren().add(logo); // vuelve a mostrar la imagen principal
    }


    @FXML
    private void cerrarSesionDashboard(ActionEvent event) {
        try {
            // Cerrar sesión
            UsuarioSession.getInstancia().cerrarSesion();

            // Cargar la vista de bienvenida
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/proyectoprogra/Visualizacion/Bienvenida.fxml"
            ));
            Parent root = loader.load();

            // Forzar scroll si existe
            ScrollPane scroll = (ScrollPane) root.lookup("#scrollPane");
            if (scroll != null) {
                scroll.setFitToWidth(true);
                scroll.setPannable(true);
                scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);


            stage.setMaximized(false);
            stage.setResizable(true);
            stage.setWidth(950);
            stage.setHeight(700);
            stage.centerOnScreen();

            stage.setTitle("Sweet Harmony - Bienvenida");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
