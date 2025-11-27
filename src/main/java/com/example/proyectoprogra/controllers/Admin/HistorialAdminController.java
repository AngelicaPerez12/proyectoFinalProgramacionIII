package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.AdminHistorialDao;
import com.example.proyectoprogra.models.Historial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HistorialAdminController  implements Initializable {
    @FXML private TableView<Historial> tablaHistorial;

    @FXML private TableColumn<Historial, Integer> colIdPedido;
    @FXML private TableColumn<Historial, String> colCliente;
    @FXML private TableColumn<Historial, String> colFechaPedido;
    @FXML private TableColumn<Historial, String> colFechaEntrega;
    @FXML private TableColumn<Historial, String> colEstado;
    @FXML private TableColumn<Historial, String> colPastel;
    @FXML private TableColumn<Historial, Integer> colCantidad;
    @FXML private TableColumn<Historial, Double> colPrecio;
    @FXML private TableColumn<Historial, Double> colTotal;
    @FXML private TableColumn<Historial, Void> colAccion; // Nueva columna



    public void initialize(URL url, ResourceBundle rb) {

        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPastel.setCellValueFactory(new PropertyValueFactory<>("pastel"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tablaHistorial.setItems(AdminHistorialDao.obtenerTodosLosPedidos());

        colAccion.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboEstado = new ComboBox<>();

            {
                comboEstado.getItems().addAll("Pendiente", "En proceso", "Listo", "Entregado");
                comboEstado.setOnAction(e -> {
                    Historial pedido = getTableView().getItems().get(getIndex());
                    String nuevoEstado = comboEstado.getValue();
                    pedido.setEstado(nuevoEstado); // Actualiza modelo en memoria

                    AdminHistorialDao.actualizarEstado(pedido.getIdPedido(), nuevoEstado);

                    tablaHistorial.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Historial pedido = getTableView().getItems().get(getIndex());
                    comboEstado.setValue(pedido.getEstado());
                    setGraphic(comboEstado);
                }
            }
        });
    }



    public void informacionUsuario(ActionEvent actionEvent) {
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

    public void refrescarTabla(ActionEvent actionEvent) {
        tablaHistorial.setItems(AdminHistorialDao.obtenerTodosLosPedidos());
        tablaHistorial.refresh();
    }
}
