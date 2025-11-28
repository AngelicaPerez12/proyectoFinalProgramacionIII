package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Historial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminHistorialDao {

    public static ObservableList<Historial> obtenerTodosLosPedidos() {
        ObservableList<Historial> lista = FXCollections.observableArrayList();

        String sql = """
            SELECT p.id_pedido, p.fecha_pedido, p.fecha_entrega, p.estado,
                   pa.nombre AS pastel, dp.cantidad, dp.precio_unitario,
                   u.nombre AS cliente
            FROM tbl_pedidos p
            JOIN tbl_pedido_detalle dp ON p.id_pedido = dp.id_pedido
            JOIN tbl_pasteles pa ON dp.id_pastel = pa.id_pastel
            JOIN tbl_usuarios u ON p.id_usuario = u.id_usuario
            ORDER BY p.id_pedido DESC
        """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Historial h = new Historial(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getDate("fecha_entrega").toLocalDate(),
                        rs.getString("estado"),
                        rs.getString("pastel"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getString("cliente")
                );

                lista.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void actualizarEstado(int idPedido, String estado) {
        String sql = "UPDATE tbl_pedidos SET estado = ? WHERE id_pedido = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- NUEVOS MÉTODOS CRUD ---------------------

    public static void agregarPedido(Historial pedido) {
        String sqlPedido = "INSERT INTO tbl_pedidos (id_usuario, fecha_pedido, fecha_entrega, estado) VALUES ((SELECT id_usuario FROM tbl_usuarios WHERE nombre = ?), ?, ?, ?)";
        String sqlDetalle = "INSERT INTO tbl_pedido_detalle (id_pedido, id_pastel, cantidad, precio_unitario) VALUES (LAST_INSERT_ID(), (SELECT id_pastel FROM tbl_pasteles WHERE nombre = ?), ?, ?)";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido);
             PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {

            // tbl_pedidos
            stmtPedido.setString(1, pedido.getCliente());
            stmtPedido.setDate(2, java.sql.Date.valueOf(pedido.getFechaPedido()));
            stmtPedido.setDate(3, java.sql.Date.valueOf(pedido.getFechaEntrega()));
            stmtPedido.setString(4, pedido.getEstado());
            stmtPedido.executeUpdate();

            // tbl_pedido_detalle
            stmtDetalle.setString(1, pedido.getPastel());
            stmtDetalle.setInt(2, pedido.getCantidad());
            stmtDetalle.setDouble(3, pedido.getPrecio());
            stmtDetalle.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void actualizarPedido(Historial pedido) {
        String sqlPedido = "UPDATE tbl_pedidos SET estado = ? WHERE id_pedido = ?";
        String sqlDetalle = "UPDATE tbl_pedido_detalle SET cantidad = ?, precio_unitario = ? WHERE id_pedido = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido);
             PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {

            // tbl_pedidos
            stmtPedido.setString(1, pedido.getEstado());
            stmtPedido.setInt(2, pedido.getIdPedido());
            stmtPedido.executeUpdate();

            // tbl_pedido_detalle
            stmtDetalle.setInt(1, pedido.getCantidad());
            stmtDetalle.setDouble(2, pedido.getPrecio());
            stmtDetalle.setInt(3, pedido.getIdPedido());
            stmtDetalle.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarPedido(int idPedido) {
        String sqlDetalle = "DELETE FROM tbl_pedido_detalle WHERE id_pedido = ?";
        String sqlPedido = "DELETE FROM tbl_pedidos WHERE id_pedido = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle);
             PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {

            stmtDetalle.setInt(1, idPedido);
            stmtDetalle.executeUpdate();

            stmtPedido.setInt(1, idPedido);
            stmtPedido.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- MÉTODO EXISTENTE ---------------------

    public static ObservableList<Historial> obtenerPedidosPendientes() {
        ObservableList<Historial> lista = FXCollections.observableArrayList();

        String sql = """
        SELECT p.id_pedido, p.fecha_pedido, p.fecha_entrega, p.estado,
               pa.nombre AS pastel, dp.cantidad, dp.precio_unitario,
               u.nombre AS cliente
        FROM tbl_pedidos p
        JOIN tbl_pedido_detalle dp ON p.id_pedido = dp.id_pedido
        JOIN tbl_pasteles pa ON dp.id_pastel = pa.id_pastel
        JOIN tbl_usuarios u ON p.id_usuario = u.id_usuario
        WHERE p.estado = 'pendiente'
        ORDER BY p.fecha_pedido DESC
    """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Historial(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getDate("fecha_entrega").toLocalDate(),
                        rs.getString("estado"),
                        rs.getString("pastel"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getString("cliente")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
