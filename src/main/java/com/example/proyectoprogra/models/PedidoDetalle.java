package com.example.proyectoprogra.models;

public class PedidoDetalle {
    private int idDetalle;
    private int idPedido;
    private int idPastel;
    private int idCategoria;
    private int idTamano;
    private int cantidad;
    private int idSabor;
    private double precioUnitario;
    private String descripcionPersonalizada;

    public PedidoDetalle(int idPastel, int idCategoria, int idTamano, int idSabor, int cantidad, double precioUnitario, String descripcionPersonalizada) {
        this.idPastel = idPastel;
        this.idCategoria = idCategoria;
        this.idTamano = idTamano;
        this.idSabor = idSabor;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descripcionPersonalizada = descripcionPersonalizada;
    }
    public int getIdPastel() { return idPastel; }
    public int getIdCategoria() { return idCategoria; }
    public int getIdTamano() { return idTamano; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public String getDescripcionPersonalizada() { return descripcionPersonalizada; }

    public int getIdSabor() {
        return idSabor;
    }
}
