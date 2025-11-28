package com.example.proyectoprogra.DAO;

import com.example.proyectoprogra.ConexionDB.ConexionDB;
import com.example.proyectoprogra.models.Opciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CmbDao {

    public static ObservableList<Opciones> obtenerCategorias() {
        ObservableList<Opciones> lista = FXCollections.observableArrayList();

        String sql = "SELECT id_categoria, nombre_categoria FROM tbl_categorias ORDER BY nombre_categoria ASC";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Opciones(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre_categoria")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    public static ObservableList<Opciones> obtenerTamanos() {
        ObservableList<Opciones> lista = FXCollections.observableArrayList();

        String sql = "SELECT id_tamano, nombre_tamano FROM tbl_tamanos ORDER BY nombre_tamano ASC";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Opciones(
                        rs.getInt("id_tamano"),
                        rs.getString("nombre_tamano")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    public static ObservableList<Opciones> obtenerSabores() {
        ObservableList<Opciones> lista = FXCollections.observableArrayList();

        String sql = "SELECT id, nombre FROM tbl_sabores ORDER BY nombre ASC";

        try (Connection conn = ConexionDB.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Opciones(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }
}
