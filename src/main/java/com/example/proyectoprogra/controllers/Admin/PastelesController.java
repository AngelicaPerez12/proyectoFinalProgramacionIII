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

        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idPastel"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idCategoria"));
        colTamano.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idTamano"));
        colSabor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idSabor"));
        colPrecio.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("precio_base"));

        listaPasteles = AdminPastelDao.obtenerTodos();

        FilteredList<Pastel> filtro = new FilteredList<>(listaPasteles, p -> true);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            String texto = newVal.toLowerCase();

            filtro.setPredicate(pastel -> {

                if (texto == null || texto.isEmpty()) return true;

                if (pastel.getNombre().toLowerCase().contains(texto)) return true;
                if (pastel.getDescripcion().toLowerCase().contains(texto)) return true;
                if (String.valueOf(pastel.getIdCategoria()).contains(texto)) return true;
                if (String.valueOf(pastel.getIdTamano()).contains(texto)) return true;
                if (String.valueOf(pastel.getIdSabor()).contains(texto)) return true;
                if (String.valueOf(pastel.getPrecioBase()).contains(texto)) return true;

                return false;
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

            stage.setMaximized(true);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void historial(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/historial-admin.fxml", e);}

    @FXML
    void inforPasteles(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/pasteles-admin.fxml",e );}

    @FXML
    void pedidos(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/pedidos-admin.fxml", e);}

    @FXML
    void resportes(ActionEvent e) {cambiarVista("/com/example/proyectoprogra/Admin/reportes-admin-view.fxml", e);}

    public void informacionUsuario(ActionEvent actionEvent) {
    }
}



