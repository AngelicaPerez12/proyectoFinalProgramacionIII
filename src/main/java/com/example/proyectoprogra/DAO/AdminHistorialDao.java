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
}
