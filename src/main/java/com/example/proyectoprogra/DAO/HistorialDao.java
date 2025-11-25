package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Historial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistorialDao {
    public static ObservableList<Historial> obtenerHistorial(int idUsuario) {
        ObservableList<Historial> lista = FXCollections.observableArrayList();

        String sql = """
            SELECT 
                p.id_pedido,
                p.fecha_pedido,
                p.fecha_entrega,
                p.estado,
                d.cantidad,
                pa.nombre AS pastel,
                pa.precio_base
            FROM tbl_pedidos p
            INNER JOIN tbl_pedido_detalle d ON p.id_pedido = d.id_pedido
            INNER JOIN tbl_pasteles pa ON d.id_pastel = pa.id_pastel
            WHERE p.id_usuario = ?
            ORDER BY p.fecha_pedido DESC;
        """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Historial(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getDate("fecha_entrega").toLocalDate(),
                        rs.getString("estado"),
                        rs.getString("pastel"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_base")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
