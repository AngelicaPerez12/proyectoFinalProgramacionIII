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
    private String cliente; // 👉 nuevo campo

    // Constructor para cliente
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

    // Constructor para el cliente normal (por si lo sigues usando)
    public Historial(int idPedido, LocalDate fechaPedido, LocalDate fechaEntrega,
                     String estado, String pastel, int cantidad, double precio) {
        this(idPedido, fechaPedido, fechaEntrega, estado, pastel, cantidad, precio, null);
    }

    public int getIdPedido() { return idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public String getEstado() { return estado; }
    public String getPastel() { return pastel; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getTotal() { return total; }

    // 👉 getters y setters del nuevo campo
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
}
