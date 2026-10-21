package com.ferreyros.servtech.dto;

import java.util.UUID;

public class UsuarioDto {
    private UUID idUsuario;
    private String username;
    private String rol;
    private String nombres;
    private String apellidos;
    private String dni;
    private String email;

    public UsuarioDto() {}

    public UsuarioDto(UUID idUsuario, String username, String rol, String nombres, String apellidos, String dni, String email) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.rol = rol;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
    }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
