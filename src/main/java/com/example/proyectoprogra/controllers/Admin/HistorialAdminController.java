package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.AdminHistorialDao;
import com.example.proyectoprogra.models.EmailUtils;
import com.example.proyectoprogra.models.Historial;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HistorialAdminController implements Initializable {
    // Campos del formulario de correo
    @FXML private VBox formularioCorreo;
    @FXML private TextField txtAsunto;
    @FXML private TextArea txtMensaje;
    @FXML private Button btnCorreo;

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
    @FXML private TableColumn<Historial, Void> colAccion;

    // Campos CRUD
    @FXML private TextField txtIdPedido;
    @FXML private TextField txtCliente;
    @FXML private TextField txtPastel;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cmbEstado;

    @FXML private Button btnAgregar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración de columnas
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

        // Configuración de ComboBox en columna Accion
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboEstado = new ComboBox<>();
            {
                comboEstado.getItems().addAll("Pendiente", "En proceso", "Listo", "Entregado");
                comboEstado.setOnAction(e -> {
                    Historial pedido = getTableView().getItems().get(getIndex());
                    if (pedido != null) {
                        String nuevoEstado = comboEstado.getValue();
                        pedido.setEstado(nuevoEstado);
                        AdminHistorialDao.actualizarEstado(pedido.getIdPedido(), nuevoEstado);
                        tablaHistorial.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Historial pedido = getTableView().getItems().get(getIndex());
                    if (pedido != null) comboEstado.setValue(pedido.getEstado());
                    setGraphic(comboEstado);
                }
            }
        });

        // Inicializar ComboBox del CRUD
        cmbEstado.getItems().addAll("Pendiente", "En proceso", "Listo", "Entregado");

        // Seleccionar fila de la tabla para llenar el panel CRUD
        tablaHistorial.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIdPedido.setText(String.valueOf(newSelection.getIdPedido()));
                txtCliente.setText(newSelection.getCliente());
                txtPastel.setText(newSelection.getPastel());
                txtCantidad.setText(String.valueOf(newSelection.getCantidad()));
                txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                cmbEstado.setValue(newSelection.getEstado());
            }
        });
    }

    // ------------------ MÉTODOS CRUD ------------------

    @FXML
    public void agregarPedido(ActionEvent event) {
        try {
            // Obtener datos desde los campos del panel CRUD
            String cliente = txtCliente.getText();
            String pastel = txtPastel.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            String estado = cmbEstado.getValue();
            LocalDate fechaPedido = LocalDate.now();
            LocalDate fechaEntrega = LocalDate.now().plusDays(2); // ejemplo de entrega

            // Crear objeto Historial correctamente (total se calcula dentro del modelo)
            Historial nuevo = new Historial(0, fechaPedido, fechaEntrega, estado, pastel, cantidad, precio, cliente);

            // Insertar en la base de datos
            AdminHistorialDao.agregarPedido(nuevo);

            // Refrescar la tabla y limpiar campos
            refrescarTabla(null);
            limpiarCampos();

        } catch (NumberFormatException e) {
            System.out.println("Error: cantidad o precio no son válidos");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error al agregar pedido");
            e.printStackTrace();
        }
    }

    @FXML
    public void actualizarPedido(ActionEvent event) {
        try {
            // Obtener datos del panel CRUD
            int id = Integer.parseInt(txtIdPedido.getText());
            String cliente = txtCliente.getText();
            String pastel = txtPastel.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            String estado = cmbEstado.getValue();

            // Crear objeto Historial usando constructor simplificado
            Historial actualizado = new Historial(id, estado, pastel, cantidad, precio, cliente);

            // Actualizar en la base de datos
            AdminHistorialDao.actualizarPedido(actualizado);

            // Refrescar tabla y limpiar campos
            refrescarTabla(null);
            limpiarCampos();

        } catch (NumberFormatException e) {
            System.out.println("Error: cantidad o precio no válidos");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error al actualizar pedido");
            e.printStackTrace();
        }
    }


    @FXML
    public void eliminarPedido(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdPedido.getText());
            AdminHistorialDao.eliminarPedido(id);
            refrescarTabla(null);
            limpiarCampos();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al eliminar pedido");
        }
    }

    private void limpiarCampos() {
        txtIdPedido.clear();
        txtCliente.clear();
        txtPastel.clear();
        txtCantidad.clear();
        txtPrecio.clear();
        cmbEstado.setValue(null);
    }

    // ------------------ RESTO DE MÉTODOS EXISTENTES ------------------

    public void refrescarTabla(ActionEvent actionEvent) {
        tablaHistorial.setItems(AdminHistorialDao.obtenerTodosLosPedidos());
        tablaHistorial.refresh();
    }

    @FXML
    public void mostrarFormularioCorreo(ActionEvent actionEvent) {
        boolean visible = formularioCorreo.isVisible();
        formularioCorreo.setVisible(!visible);
        formularioCorreo.setManaged(!visible);

        // Si hay un pedido seleccionado, rellenar automáticamente
        Historial seleccionado = tablaHistorial.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            txtAsunto.setText("Información de su pedido #" + seleccionado.getIdPedido());
            txtMensaje.setText("Hola " + seleccionado.getCliente() + ",\n\n" +
                    "Su pedido de " + seleccionado.getCantidad() + " " + seleccionado.getPastel() +
                    " está actualmente en estado: " + seleccionado.getEstado() + ".\n" +
                    "Fecha de entrega estimada: " + seleccionado.getFechaEntrega() + ".\n\n" +
                    "¡Gracias por su preferencia!");
        }
    }


    @FXML
    public void enviarCorreo(ActionEvent actionEvent) {
        String cliente = txtCliente.getText(); // Aquí puedes usar el email real del cliente
        String asunto = txtAsunto.getText();
        String mensaje = txtMensaje.getText();

        try {
            // Enviar correo usando EmailUtils
            EmailUtils.enviarCorreo(cliente, asunto, mensaje);

            // Mensaje de confirmación (opcional)
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Correo enviado");
            alerta.setHeaderText(null);
            alerta.setContentText("El correo ha sido enviado correctamente a " + cliente);
            alerta.showAndWait();

            // Ocultar y limpiar formulario después de enviar
            txtAsunto.clear();
            txtMensaje.clear();
            formularioCorreo.setVisible(false);
            formularioCorreo.setManaged(false);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se pudo enviar el correo");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
