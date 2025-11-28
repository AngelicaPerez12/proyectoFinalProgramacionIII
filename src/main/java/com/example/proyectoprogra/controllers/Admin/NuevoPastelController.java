package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.AdminPastelDao;
import com.example.proyectoprogra.DAO.CmbDao;
import com.example.proyectoprogra.models.Opciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NuevoPastelController { @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;

    @FXML private ComboBox<Opciones> comboCategoria;
    @FXML private ComboBox<Opciones> comboTamano;
    @FXML private ComboBox<Opciones> comboSabor;

    @FXML
    public void initialize() {
        comboCategoria.setItems(CmbDao.obtenerCategorias());
        comboTamano.setItems(CmbDao.obtenerTamanos());
        comboSabor.setItems(CmbDao.obtenerSabores());
    }

    @FXML
    public void guardarPastel(ActionEvent e) {
        try {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());

            Opciones categoria = comboCategoria.getValue();
            Opciones tamano = comboTamano.getValue();
            Opciones sabor = comboSabor.getValue();

            if (categoria == null || tamano == null || sabor == null) {
                throw new Exception("Debes seleccionar categoría, tamaño y sabor");
            }

            AdminPastelDao.insertarPastel(
                    nombre,
                    descripcion,
                    categoria.getId(),
                    tamano.getId(),
                    sabor.getId(),
                    precio
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Pastel agregado correctamente");
            alert.showAndWait();

            cerrar(e);

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Datos inválidos");
            alert.setContentText(ex.getMessage());
            alert.show();
        }
    }

    @FXML
    public void cancelar(ActionEvent e) {
        cerrar(e);
    }

    private void cerrar(ActionEvent e) {
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }
}
