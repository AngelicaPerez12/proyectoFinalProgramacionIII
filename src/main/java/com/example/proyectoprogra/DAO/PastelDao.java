package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Pastel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PastelDao {
    public List<Pastel> obtenerPorCategoriaYTamano(int idCategoria, int idTamano) {
        List<Pastel> lista = new ArrayList<>();
        String sql = "SELECT id_pastel, nombre, descripcion, id_categoria, id_tamano, precio_base, id_sabor FROM tbl_pasteles WHERE id_categoria = ? AND id_tamano = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.setInt(2, idTamano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Pastel(
                            rs.getInt("id_pastel"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("categoria"),
                            rs.getString("tamano"),
                            rs.getDouble("precio_base"),
                            rs.getString("id_sabor")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Pastel obtenerPorId(int idPastel) {
        String sql = "SELECT id_pastel, nombre, descripcion, id_categoria, id_tamano, precio_base, id_sabor FROM tbl_pasteles WHERE id_pastel = ?";
        try (Connection conn = ConexionDB.getConection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPastel);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pastel(
                            rs.getInt("id_pastel"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("id_categoria"),
                            rs.getString("id_tamano"),
                            rs.getDouble("precio_base"),
                            rs.getString("id_sabor")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
