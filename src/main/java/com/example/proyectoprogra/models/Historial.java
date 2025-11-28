package com.example.proyectoprogra.models;

import java.time.LocalDate;

public class Historial {

    private int idPedido;
    private LocalDate fechaPedido;
    private LocalDate fechaEntrega;
    private String estado;
    private String pastel;
    private int cantidad;
    private double precio;
    private double total;
    private String cliente;

    public Historial(int idPedido, LocalDate fechaPedido, LocalDate fechaEntrega,
                     String estado, String pastel, int cantidad, double precio, String cliente) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.pastel = pastel;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = cantidad * precio;
        this.cliente = cliente;
    }

    public Historial(int idPedido, LocalDate fechaPedido, LocalDate fechaEntrega,
                     String estado, String pastel, int cantidad, double precio) {
        this(idPedido, fechaPedido, fechaEntrega, estado, pastel, cantidad, precio, null);
    }

    // ----------- Getters -----------
    public int getIdPedido() { return idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public String getEstado() { return estado; }
    public String getPastel() { return pastel; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getTotal() { return total; }
    public String getCliente() { return cliente; }

    // ----------- Setters -----------
    public void setCliente(String cliente) { this.cliente = cliente; }

    public void setEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.total = this.cantidad * this.precio; // recalcula total
    }

    public void setPrecio(double precio) {
        this.precio = precio;
        this.total = this.cantidad * this.precio; // recalcula total
    }
    // Constructor para actualizar pedido (sin modificar fechas)
    public Historial(int idPedido, String estado, String pastel, int cantidad, double precio, String cliente) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.pastel = pastel;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = cantidad * precio;
        this.cliente = cliente;
    }
}
