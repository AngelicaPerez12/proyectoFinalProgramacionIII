package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    // Obtener id del rol "Cliente"
    private static int obtenerIdRolCliente() {
        String sql = "SELECT id_rol FROM tbl_roles WHERE nombre_rol='Cliente'";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id_rol");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Si no existe, retornar 0
    }

    // Agregar usuario (siempre Cliente)
    public static boolean agregarUsuario(Usuario u) {
        int idRol = obtenerIdRolCliente();
        if (idRol == 0) return false;

        String sql = "INSERT INTO tbl_usuarios (nombre, apellido, correo, contraseña, id_rol) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getApellido());
            stmt.setString(3, u.getCorreo());
            stmt.setString(4, u.getContraseña() != null ? u.getContraseña() : "123456");
            stmt.setInt(5, idRol);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Obtener todos los usuarios
    public static List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.correo, u.contraseña, r.nombre_rol " +
                "FROM tbl_usuarios u " +
                "JOIN tbl_roles r ON u.id_rol = r.id_rol";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("nombre_rol")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Actualizar usuario
    public static boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE tbl_usuarios SET nombre = ?, apellido = ?, correo = ? WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getApellido());
            stmt.setString(3, u.getCorreo());
            stmt.setInt(4, u.getIdUsuario());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean eliminarUsuario(int idUsuario) {
        try (Connection conn = ConexionDB.getConection()) {
            conn.setAutoCommit(false); // Inicia transacción

            // 1. Obtener los pedidos del usuario
            String sqlPedidos = "SELECT id_pedido FROM tbl_pedidos WHERE id_usuario = ?";
            List<Integer> pedidos = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlPedidos)) {
                stmt.setInt(1, idUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        pedidos.add(rs.getInt("id_pedido"));
                    }
                }
            }

            // 2. Eliminar detalles de pedidos
            String sqlDetalle = "DELETE FROM tbl_pedido_detalle WHERE id_pedido = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalle)) {
                for (Integer idPedido : pedidos) {
                    stmt.setInt(1, idPedido);
                    stmt.executeUpdate();
                }
            }

            // 3. Eliminar pedidos
            String sqlEliminarPedidos = "DELETE FROM tbl_pedidos WHERE id_usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEliminarPedidos)) {
                stmt.setInt(1, idUsuario);
                stmt.executeUpdate();
            }

            // 4. Eliminar usuario
            String sqlUsuario = "DELETE FROM tbl_usuarios WHERE id_usuario = ?";
            int rows;
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setInt(1, idUsuario);
                rows = stmt.executeUpdate();
            }

            conn.commit(); // Confirmar todo
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
