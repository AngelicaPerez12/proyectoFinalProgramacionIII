package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.AdminPastelDao;
import com.example.proyectoprogra.models.Pastel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class PastelesController {

    @FXML private TableView<Pastel> tablaPasteles;
    @FXML private TextField txtBuscar;

    @FXML private TableColumn<Pastel, Integer> colId;
    @FXML private TableColumn<Pastel, String> colNombre;
    @FXML private TableColumn<Pastel, String> colDescripcion;
    @FXML private TableColumn<Pastel, Integer> colCategoria;
    @FXML private TableColumn<Pastel, Integer> colTamano;
    @FXML private TableColumn<Pastel, Integer> colSabor;
    @FXML private TableColumn<Pastel, Double> colPrecio;

    private ObservableList<Pastel> listaPasteles;

    @FXML
    public void initialize() {
        // Configuración de columnas
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idPastel"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("categoria"));
        colTamano.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tamano"));
        colSabor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sabor"));

        colPrecio.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("precioBase"));

        listaPasteles = AdminPastelDao.obtenerTodos();

        // Filtro
        FilteredList<Pastel> filtro = new FilteredList<>(listaPasteles, p -> true);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            String texto = (newVal == null ? "" : newVal.toLowerCase());

            filtro.setPredicate(pastel -> {
                if (texto.isEmpty()) return true;

                return pastel.getNombre().toLowerCase().contains(texto) ||
                        pastel.getDescripcion().toLowerCase().contains(texto) ||
                        String.valueOf(pastel.getCategoria()).contains(texto) ||
                        String.valueOf(pastel.getTamano()).contains(texto) ||
                        String.valueOf(pastel.getSabor()).contains(texto) ||
                        String.valueOf(pastel.getPrecioBase()).contains(texto);
            });
        });

        SortedList<Pastel> sorted = new SortedList<>(filtro);
        sorted.comparatorProperty().bind(tablaPasteles.comparatorProperty());
        tablaPasteles.setItems(sorted);
    }

    private void cambiarVista(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);


            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void historial(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/historial-admin.fxml", e); }

    @FXML
    void inforPasteles(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/pasteles-admin.fxml", e); }

    @FXML
    void pedidos(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/pedidos-admin.fxml", e); }

    @FXML
    void resportes(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/reportes-admin-view.fxml", e); }

    public void informacionUsuario(ActionEvent actionEvent) {}

    public void crearnuevoproducto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Admin/nuevo-pastel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
