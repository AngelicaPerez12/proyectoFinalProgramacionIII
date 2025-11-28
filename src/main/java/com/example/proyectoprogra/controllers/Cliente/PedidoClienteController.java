package com.example.proyectoprogra.controllers.Cliente;

import com.example.proyectoprogra.models.EmailUtils;
import com.example.proyectoprogra.models.UsuarioSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.example.proyectoprogra.DAO.PastelDao;
import com.example.proyectoprogra.DAO.PedidoDao;
import com.example.proyectoprogra.models.Pastel;
import com.example.proyectoprogra.models.PedidoDetalle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoClienteController {

    @FXML private Button btnconfirmarpedido;
    @FXML private ComboBox<String> cmbtamañopasteles;
    @FXML private ComboBox<String> cmbtipodepasteles;
    @FXML private DatePicker dtfechadeentrega;
    @FXML private TextField txtcorreocliente;
    @FXML private TextArea txtdescripcionpersonalizada;
    @FXML private TextField txtnombrecliente;
    @FXML private ComboBox<String> cmbsaborpastel;

    private int usuarioLogueadoId;

    @FXML
    public void initialize() {
        cargarCategoriasDesdeDB();
        cargarTamanosDesdeDB();
        cargarSaboresDesdeDB();

        var ses = UsuarioSession.getInstancia();
        if (ses != null && ses.getIdUsuario() != 0) {
            txtnombrecliente.setText(ses.getNombre() != null ? ses.getNombre() : "");
            txtcorreocliente.setText(ses.getCorreo() != null ? ses.getCorreo() : "");
        }
        usuarioLogueadoId = UsuarioSession.getInstancia().getIdUsuario();
    }

    private void cargarCategoriasDesdeDB() {
        try (var conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             var ps = conn.prepareStatement("SELECT nombre_categoria FROM tbl_categorias ORDER BY id_categoria");
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbtipodepasteles.getItems().add(rs.getString("nombre_categoria"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarTamanosDesdeDB() {
        try (var conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             var ps = conn.prepareStatement("SELECT nombre_tamano FROM tbl_tamanos ORDER BY id_tamano");
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbtamañopasteles.getItems().add(rs.getString("nombre_tamano"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }
    private void cargarSaboresDesdeDB() {
        try (Connection conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM tbl_sabores ORDER BY id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbsaborpastel.getItems().add(rs.getString("nombre"));
            }

            System.out.println("Sabores cargados: " + cmbsaborpastel.getItems().size()); // debug

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int obtenerIdSabor(String nombre) throws SQLException {
        try (var conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             var ps = conn.prepareStatement("SELECT id FROM tbl_sabores WHERE nombre = ?")) {

            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Sabor no encontrado");
    }


    @FXML
    private void confirmarpedido() {
        UsuarioSession ses = UsuarioSession.getInstancia();
        if (ses == null || ses.getIdUsuario() == 0) {
            mostrarAlerta("No has iniciado sesión. Por favor inicia sesión para hacer pedidos.");
            return;
        }

        String tipo = cmbtipodepasteles.getValue();
        String tamano = cmbtamañopasteles.getValue();
        LocalDate fechaEntrega = dtfechadeentrega.getValue();
        String descripcion = txtdescripcionpersonalizada.getText();
        String sabor = cmbsaborpastel.getValue();


        if (tipo == null || tamano == null || fechaEntrega == null) {
            mostrarAlerta("Complete los campos obligatorios");
            return;
        }

        try {
            int idCategoria = obtenerIdCategoria(tipo);
            int idTamano = obtenerIdTamano(tamano);

            PastelDao pastelDAO = new PastelDao();
            List<Pastel> matches = pastelDAO.obtenerPorCategoriaYTamano(idCategoria, idTamano);
            if (matches.isEmpty()) {
                mostrarAlerta("No hay pasteles disponibles para la categoría/tamaño seleccionados.");
                return;
            }
            if (sabor == null) {
                mostrarAlerta("Debe seleccionar un sabor para el pastel.");
                return;
            }

            Pastel elegido = matches.get(0);
            double precioUnitario = elegido.getPrecioBase();
            String saborSeleccionado = cmbsaborpastel.getValue();

            if (saborSeleccionado == null) {
                mostrarAlerta("Debe seleccionar un sabor para el pastel.");
                return;
            }

            int idSabor = obtenerIdSabor(saborSeleccionado);

            List<PedidoDetalle> detalles = new ArrayList<>();
            detalles.add(new PedidoDetalle(
                    elegido.getIdPastel(),
                    idCategoria,
                    idTamano,
                    idSabor,
                    1,
                    precioUnitario,
                    descripcion
            ));

            PedidoDao pedidoDAO = new PedidoDao();
            int idPedido = pedidoDAO.insertarPedido(ses.getIdUsuario(), fechaEntrega, detalles);

            EmailUtils.enviarCorreo(
                    ses.getCorreo(),
                    "Confirmación de Pedido #" + idPedido,
                    "Hola " + ses.getNombre() + ",\n\n" +
                            "Tu pedido ha sido confirmado.\n" +
                            "Pastel: " + elegido.getNombre() + "\n" +
                            "Categoría: " + tipo + "\n" +
                            "Tamaño: " + tamano + "\n" +
                            "Sabor: " + sabor + "\n" +
                            "Entrega: " + fechaEntrega + "\n" +
                            "Total: $" + precioUnitario + "\n\n" +
                            "Gracias por tu compra :)"
            );

            // Abrir ticket UNA sola vez
            abrirTicket(idPedido, ses.getIdUsuario(), ses.getNombre(), ses.getCorreo(),
                    elegido.getNombre(), tipo, tamano, sabor, precioUnitario, descripcion, fechaEntrega);

            mostrarAlerta("Pedido creado exitosamente. ID: " + idPedido);

        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error al guardar el pedido: " + ex.getMessage());
        }
    }

    private int obtenerIdCategoria(String nombre) throws SQLException {
        try (var conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             var ps = conn.prepareStatement("SELECT id_categoria FROM tbl_categorias WHERE nombre_categoria = ?")) {

            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_categoria");
            }
        }
        throw new SQLException("Categoría no encontrada");
    }

    private int obtenerIdTamano(String nombre) throws SQLException {
        try (var conn = com.example.proyectoprogra.ConexionDB.ConexionDB.getConection();
             var ps = conn.prepareStatement("SELECT id_tamano FROM tbl_tamanos WHERE nombre_tamano = ?")) {

            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_tamano");
            }
        }
        throw new SQLException("Tamaño no encontrado");
    }

    private void abrirTicket(int idPedido, int idUsuario, String nombre, String correo,
                             String nombrePastel, String tipo, String tamano,String sabor, double precio,
                             String descripcion, LocalDate fechaEntrega) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/ticket.fxml"));
            VBox root = loader.load();

            TicketController ctrl = loader.getController();
            ctrl.setDatos(idPedido, idUsuario, nombre, correo, nombrePastel, tipo, tamano, sabor, precio, descripcion, fechaEntrega);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ticket de Pedido");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el ticket.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }


    public void vercatalogo(ActionEvent event) {
        abrirVista(event, "/com/example/proyectoprogra/Cliente/catalogo-cliente.fxml");
    }

    public void hacerpedido(ActionEvent event) {
        abrirVista(event, "/com/example/proyectoprogra/Cliente/pedido-cliente.fxml");
    }

    public void historial(ActionEvent event) {
        abrirVista(event, "/com/example/proyectoprogra/Cliente/historial-cliente.fxml");
    }

    public void perfil(ActionEvent event) {
        abrirVista(event, "/com/example/proyectoprogra/Cliente/perfil-cliente.fxml");
    }

    private void abrirVista(ActionEvent event, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
