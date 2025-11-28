package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Pastel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPastelDao {

    public static ObservableList<Pastel> obtenerTodos() {
        ObservableList<Pastel> lista = FXCollections.observableArrayList();

        String sql = """
        SELECT p.id_pastel, p.nombre, p.descripcion,
               c.nombre_categoria AS categoria,
               t.nombre_tamano AS tamano,
               s.nombre AS sabor,
               p.precio_base
        FROM tbl_pasteles p
        INNER JOIN tbl_categorias c ON p.id_categoria = c.id_categoria
        INNER JOIN tbl_tamanos t ON p.id_tamano = t.id_tamano
        INNER JOIN tbl_sabores s ON p.id_sabor = s.id
        ORDER BY p.id_pastel ASC
    """;


        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Pastel(
                        rs.getInt("id_pastel"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("categoria"),
                        rs.getString("tamano"),
                        rs.getDouble("precio_base"),
                        rs.getString("sabor")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    public static void insertarPastel(String nombre, String descripcion, int idCategoria, int idTamano, int idSabor, double precio) {

        String sql = """
        INSERT INTO tbl_pasteles 
        (nombre, descripcion, id_categoria, id_tamano, id_sabor, precio_base) 
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, idCategoria);
            stmt.setInt(4, idTamano);
            stmt.setInt(5, idSabor);
            stmt.setDouble(6, precio);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
