package com.example.proyectoprogra.models;

public class Usuario {
private int idUsuario;
private String nombre;
private String apellido;
private String correo;
private String contraseña;
private String rol;

public Usuario(int idUsuario, String nombre, String apellido, String correo, String contraseña, String rol) {
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.apellido = apellido;
    this.correo = correo;
    this.contraseña = contraseña;
    this.rol = rol;
}

public int getIdUsuario() { return idUsuario; }
public String getNombre() { return nombre; }
public String getApellido() { return apellido; }
public String getCorreo() { return correo; }
public String getContraseña() { return contraseña; }
public String getRol() { return rol; }

public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
public void setNombre(String nombre) { this.nombre = nombre; }
public void setApellido(String apellido) { this.apellido = apellido; }
public void setCorreo(String correo) { this.correo = correo; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
public void setRol(String rol) { this.rol = rol; }
}

