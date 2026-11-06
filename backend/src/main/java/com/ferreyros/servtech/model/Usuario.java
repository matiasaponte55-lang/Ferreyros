package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/* Entidad Usuario: Representa las cuentas del sistema con roles para el control RBAC */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", updatable = false, nullable = false)
    private UUID idUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "rol", nullable = false, length = 30)
    private String rol;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn = OffsetDateTime.now();

    public Usuario() {}

    public Usuario(String username, String passwordHash, String rol, String nombres, String apellidos, String dni, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.activo = true;
        this.creadoEn = OffsetDateTime.now();
    }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

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

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }
}
