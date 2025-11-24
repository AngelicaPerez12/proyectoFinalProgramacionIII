package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.models.PedidoDetalle;
import com.example.proyectoprogra.ConexionDB.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
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
}
