package com.example.proyectoprogra.controllers.Cliente;

import com.example.proyectoprogra.DAO.HistorialDao;
import com.example.proyectoprogra.models.Historial;
import com.example.proyectoprogra.models.UsuarioSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class HistorialClienteController {
    @FXML private TableView<Historial> tablaHistorial;

    @FXML private TableColumn<Historial, Integer> colIdPedido;
    @FXML private TableColumn<Historial, String> colFechaPedido;
    @FXML private TableColumn<Historial, String> colFechaEntrega;
    @FXML private TableColumn<Historial, String> colEstado;
    @FXML private TableColumn<Historial, String> colPastel;
    @FXML private TableColumn<Historial, Integer> colCantidad;
    @FXML private TableColumn<Historial, Double> colPrecio;
    @FXML private TableColumn<Historial, Double> colTotal;

    @FXML private TableColumn<Historial, Void> colAcciones;

    @FXML
    public void initialize() {
        // Configurar columnas
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPastel.setCellValueFactory(new PropertyValueFactory<>("pastel"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        int idUsuario = UsuarioSession.getInstancia().getIdUsuario();

        if (idUsuario == 0) {
            System.out.println("⚠ No hay usuario en sesión");
            return;
        }

        // Cargar historial del usuario
        tablaHistorial.setItems(HistorialDao.obtenerHistorial(idUsuario));

        // Agregar botón cancelar
        agregarBotonCancelar();
    }

    private void agregarBotonCancelar() {
        colAcciones.setCellFactory(columna -> new TableCell<Historial, Void>() {
            private final Button btn = new Button("Cancelar");

            {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");

                btn.setOnAction(e -> {
                    Historial seleccionado = getTableView().getItems().get(getIndex());
                    String estado = seleccionado.getEstado();

                    // Verificar si se puede cancelar
                    if (estado.equalsIgnoreCase("En proceso") ||
                            estado.equalsIgnoreCase("Listo") ||
                            estado.equalsIgnoreCase("Entregado")) {

                        Alert alerta = new Alert(Alert.AlertType.WARNING);
                        alerta.setTitle("No se puede cancelar");
                        alerta.setHeaderText("El pedido no puede ser cancelado");
                        alerta.setContentText("El estado del pedido es '" + estado + "'.");
                        alerta.show();
                        return;
                    }

                    // Confirmación de cancelación
                    Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmar.setTitle("Confirmación");
                    confirmar.setHeaderText("¿Cancelar pedido?");
                    confirmar.setContentText("Esta acción eliminará el pedido del historial.");

                    if (confirmar.showAndWait().get() == ButtonType.OK) {
                        boolean exito = HistorialDao.cancelarPedido(seleccionado.getIdPedido());
                        if (exito) {
                            getTableView().getItems().remove(seleccionado);

                            Alert ok = new Alert(Alert.AlertType.INFORMATION);
                            ok.setTitle("Pedido cancelado");
                            ok.setHeaderText(null);
                            ok.setContentText("El pedido ha sido cancelado exitosamente.");
                            ok.show();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    // Métodos de navegación
    public void vercatalogo(ActionEvent actionEvent) {
        abrirVista("/com/example/proyectoprogra/Cliente/catalogo-cliente.fxml", actionEvent);
    }

    public void hacerpedido(ActionEvent actionEvent) {
        abrirVista("/com/example/proyectoprogra/Cliente/pedido-cliente.fxml", actionEvent);
    }

    public void historial(ActionEvent actionEvent) {
        abrirVista("/com/example/proyectoprogra/Cliente/historial-cliente.fxml", actionEvent);
    }

    public void perfil(ActionEvent actionEvent) {
        abrirVista("/com/example/proyectoprogra/Cliente/perfil-cliente.fxml", actionEvent);
    }

    // Método auxiliar para abrir nuevas vistas
    private void abrirVista(String fxml, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
