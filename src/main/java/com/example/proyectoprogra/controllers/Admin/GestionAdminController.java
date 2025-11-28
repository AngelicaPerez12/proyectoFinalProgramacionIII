package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.UsuarioService;
import com.example.proyectoprogra.models.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestionAdminController implements Initializable {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdUsuario()).asObject());
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colApellido.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getApellido()));
        colCorreo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCorreo()));
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRol()));

        cargarUsuarios();

        // Llenar formulario al seleccionar fila
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if(newSel != null) {
                txtNombre.setText(newSel.getNombre());
                txtApellido.setText(newSel.getApellido());
                txtCorreo.setText(newSel.getCorreo());
            }
        });
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = UsuarioService.obtenerTodos();
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarios);
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void agregarUsuario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();

        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Complete todos los campos");
            alert.show();
            return;
        }

        Usuario nuevo = new Usuario(
                0,          // id temporal
                nombre,
                apellido,
                correo,
                "123456",   // contraseña por defecto
                "Cliente"   // rol fijo
        );

        boolean agregado = UsuarioService.agregarUsuario(nuevo);

        if(agregado) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario Cliente agregado correctamente");
            alert.show();
            cargarUsuarios();
            limpiarCampos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al agregar usuario");
            alert.show();
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione un usuario para actualizar");
            alert.show();
            return;
        }

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();

        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Complete todos los campos");
            alert.show();
            return;
        }

        Usuario actualizado = new Usuario(
                seleccionado.getIdUsuario(),
                nombre,
                apellido,
                correo,
                seleccionado.getContraseña(),  // mantener contraseña
                seleccionado.getRol()          // mantener rol
        );

        boolean exito = UsuarioService.actualizarUsuario(actualizado);

        if(exito) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario actualizado correctamente");
            alert.show();
            cargarUsuarios();
            limpiarCampos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al actualizar usuario");
            alert.show();
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione un usuario para eliminar");
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desea eliminar al usuario " + seleccionado.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if(confirm.getResult() == ButtonType.YES) {
            boolean eliminado = UsuarioService.eliminarUsuario(seleccionado.getIdUsuario());
            if(eliminado) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario eliminado correctamente");
                alert.show();
                cargarUsuarios();
                limpiarCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al eliminar usuario");
                alert.show();
            }
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
    }
}
