package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.models.Historial;
import com.example.proyectoprogra.models.PedidoDetalle;
import com.example.proyectoprogra.ConexionDB.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {
    public int insertarPedido(int idUsuario, LocalDate fechaEntrega, List<PedidoDetalle> detalles) throws SQLException {
        String sqlInsertPedido = "INSERT INTO tbl_pedidos (id_usuario, fecha_entrega, estado) VALUES (?, ?, 'pendiente') RETURNING id_pedido";
        String sqlInsertDetalle = "INSERT INTO tbl_pedido_detalle (id_pedido, id_pastel, id_categoria, id_tamano, cantidad, precio_unitario, descripcion_personalizada) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psDetalle = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConection();
            conn.setAutoCommit(false);

            psPedido = conn.prepareStatement(sqlInsertPedido);
            psPedido.setInt(1, idUsuario);
            psPedido.setDate(2, Date.valueOf(fechaEntrega));
            rs = psPedido.executeQuery();

            int idPedido;
            if (rs.next()) {
                idPedido = rs.getInt("id_pedido");
            } else {
                throw new SQLException("No se obtuvo id_pedido.");
            }

            psDetalle = conn.prepareStatement(sqlInsertDetalle);
            for (PedidoDetalle d : detalles) {
                psDetalle.setInt(1, idPedido);
                psDetalle.setInt(2, d.getIdPastel());
                psDetalle.setInt(3, d.getIdCategoria());
                psDetalle.setInt(4, d.getIdTamano());
                psDetalle.setInt(5, d.getCantidad());
                psDetalle.setDouble(6, d.getPrecioUnitario());
                psDetalle.setString(7, d.getDescripcionPersonalizada());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            conn.commit();
            return idPedido;

        } catch (SQLException ex) {
            if (conn != null) try { conn.rollback(); } catch (SQLException e) { /*ignore*/ }
            throw ex;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (psPedido != null) try { psPedido.close(); } catch (SQLException e) {}
            if (psDetalle != null) try { psDetalle.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
    public List<Historial> obtenerHistorialUsuario(int idUsuario) {
        List<Historial> lista = new ArrayList<>();

        String sql = """
        SELECT p.id_pedido, p.fecha_pedido, p.fecha_entrega, p.estado,
               pa.nombre_pastel, d.cantidad, d.precio_unitario
        FROM tbl_pedidos p
        INNER JOIN tbl_pedido_detalle d ON p.id_pedido = d.id_pedido
        INNER JOIN tbl_pasteles pa ON d.id_pastel = pa.id_pastel
        WHERE p.id_usuario = ?
        ORDER BY p.fecha_pedido DESC
        """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Historial(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getDate("fecha_entrega").toLocalDate(),
                        rs.getString("estado"),
                        rs.getString("nombre_pastel"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
