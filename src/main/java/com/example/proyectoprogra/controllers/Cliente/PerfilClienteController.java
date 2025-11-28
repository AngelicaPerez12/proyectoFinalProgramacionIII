package com.example.proyectoprogra.controllers.Cliente;

import com.example.proyectoprogra.DAO.UsuarioDao;
import com.example.proyectoprogra.models.Usuario;
import com.example.proyectoprogra.models.UsuarioSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PerfilClienteController {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtRol;

    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        UsuarioSession session = UsuarioSession.getInstancia();
        System.out.println("ID session: " + session.getIdUsuario());

        usuarioActual = UsuarioDao.obtenerUsuarioPorId(session.getIdUsuario());

        System.out.println("usuarioActual: " + usuarioActual);

        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            txtApellido.setText(usuarioActual.getApellido());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtRol.setText(usuarioActual.getRol());
        }
    }

    @FXML
    private void guardarCambios() {

        usuarioActual.setNombre(txtNombre.getText());
        usuarioActual.setApellido(txtApellido.getText());
        usuarioActual.setCorreo(txtCorreo.getText());

        boolean actualizado = UsuarioDao.actualizarUsuario(usuarioActual);

        if (actualizado) {
            mostrarMensaje("Cambios guardados", "Tu información se actualizó correctamente.");
        } else {
            mostrarMensaje("Error", "No se pudo actualizar la información.");
        }
    }

    @FXML
    private void cerrarSesion() {
        UsuarioSession.getInstancia().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/proyectoprogra/Visualizacion/login-view.fxml"));
            Parent root = loader.load();
            txtNombre.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        private void mostrarMensaje (String titulo, String contenido){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(titulo);
            alert.setContentText(contenido);
            alert.showAndWait();
        }

    public void vercatalogo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/catalogo-cliente.fxml"));
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

    public void hacerpedido(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/pedido-cliente.fxml"));
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

    public void historial(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/historial-cliente.fxml"));
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

    public void perfil(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/perfil-cliente.fxml"));
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
}