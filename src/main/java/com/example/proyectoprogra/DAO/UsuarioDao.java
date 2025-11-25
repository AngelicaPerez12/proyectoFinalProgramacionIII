package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDao {

    public static Usuario obtenerUsuarioPorId(int id) {
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.correo, u.contraseña, r.nombre_rol " +
                "FROM tbl_usuarios u " +
                "JOIN tbl_roles r ON u.id_rol = r.id_rol " +
                "WHERE u.id_usuario = ?";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("nombre_rol")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE tbl_usuarios SET nombre=?, apellido=?, correo=?, contraseña=? WHERE id_usuario=?";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getApellido());
            stmt.setString(3, u.getCorreo());
            stmt.setString(4, u.getContraseña());
            stmt.setInt(5, u.getIdUsuario());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
