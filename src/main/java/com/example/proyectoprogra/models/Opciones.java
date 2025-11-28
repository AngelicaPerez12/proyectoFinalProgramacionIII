package com.example.proyectoprogra.models;

public class Opciones {

    private int id;
    private String nombre;

    public Opciones(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
