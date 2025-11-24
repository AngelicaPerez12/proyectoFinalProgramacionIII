package com.example.proyectoprogra.models;

public class Pastel {
    private int idPastel;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private int idTamano;
    private double precioBase;
    private int idSabor;

    public Pastel(int idPastel, String nombre, String descripcion, int idCategoria, int idTamano, double precioBase, int idSabor) {
        this.idPastel = idPastel;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.idTamano = idTamano;
        this.precioBase = precioBase;
        this.idSabor = idSabor;
    }

    public int getIdPastel() { return idPastel; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getIdCategoria() { return idCategoria; }
    public int getIdTamano() { return idTamano; }
    public double getPrecioBase() { return precioBase; }
    public int getIdSabor() { return idSabor; }
}
