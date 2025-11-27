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
            SELECT id_pastel, nombre, descripcion, id_categoria, 
                   id_tamano, precio_base, id_sabor
            FROM tbl_pasteles
            ORDER BY id_pastel ASC
        """;

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Pastel(
                        rs.getInt("id_pastel"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("id_categoria"),
                        rs.getInt("id_tamano"),
                        rs.getDouble("precio_base"),
                        rs.getInt("id_sabor")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
