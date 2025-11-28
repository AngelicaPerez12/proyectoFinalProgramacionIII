package com.example.proyectoprogra.models;

public class Pastel {
    private int idPastel;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String tamano;
    private double precioBase;
    private String sabor;

    public Pastel(int idPastel, String nombre, String descripcion,
                  String categoria, String tamano,
                  double precioBase, String sabor) {

        this.idPastel = idPastel;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.tamano = tamano;
        this.precioBase = precioBase;
        this.sabor = sabor;
    }

    public int getIdPastel() { return idPastel; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public String getTamano() { return tamano; }
    public double getPrecioBase() { return precioBase; }
    public String getSabor() { return sabor; }
}
