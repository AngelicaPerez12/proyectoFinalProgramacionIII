package com.example.proyectoprogra.controllers.Cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.time.LocalDate;


public class TicketController {
    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblId;

    @FXML
    private Label lblCliente;

    @FXML
    private Label lblCorreo;

    @FXML
    private Label lblPastel;

    @FXML
    private Label lblTipoTamano;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblFechaEntrega;

    @FXML private Label lblPrecio;

    private final String LOGO_PATH = "file:/mnt/data/cap.png";

    @FXML
    public void initialize() {
        try {
            Image img = new Image(LOGO_PATH, true);
            imgLogo.setImage(img);
        } catch (Exception e) {
        }
    }

    public void setDatos(int idPedido, int idUsuario, String nombre, String correo,
                         String nombrePastel, String tipo, String tamano, double precio,
                         String descripcion, LocalDate fechaEntrega) {

        lblTitulo.setText("Ticket de Pedido");
        lblId.setText("ID Pedido: " + idPedido);
        lblCliente.setText("Cliente: " + (nombre == null || nombre.isBlank() ? "-" : nombre));
        lblCorreo.setText("Correo: " + (correo == null || correo.isBlank() ? "-" : correo));
        lblPastel.setText("Pastel: " + nombrePastel);
        lblTipoTamano.setText("Tipo: " + tipo + " | Tamaño: " + tamano);
        lblDescripcion.setText("Descripción: " + (descripcion == null || descripcion.isBlank() ? "-" : descripcion));
        lblFechaEntrega.setText("Fecha entrega: " + fechaEntrega.toString());
        lblPrecio.setText(String.format("Precio: $%.2f", precio));
    }

    @FXML
    private void cerrar() {
        Stage st = (Stage) lblTitulo.getScene().getWindow();
        st.close();
    }
}
