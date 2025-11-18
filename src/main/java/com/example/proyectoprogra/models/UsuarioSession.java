package com.example.proyectoprogra.models;

public class UsuarioSession {
    private static UsuarioSession instancia;

    private int idUsuario;
    private String nombre;
    private String correo;
    private String rol;

    private UsuarioSession() {}

    public static UsuarioSession getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioSession();
        }
        return instancia;
    }

    public void iniciarSesion(int id, String nombre, String correo, String rol) {
        this.idUsuario = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }

    public void cerrarSesion() {
        instancia = null;
    }
}
