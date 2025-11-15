package com.example.proyectoprogra.ConexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String url= "jdbc:postgresql://localhost:5432/db_proyectoProgra";

    private static final String USER = "postgres";

    private static final String PASS = "admin123";

    public static Connection getConection(){
        try {
            Connection conectar = DriverManager.getConnection(url, USER, PASS);
            System.out.println("Conectado a la base");
            return conectar;
        } catch (SQLException e) {
            throw new RuntimeException(e){

            };
        }
    }
}
