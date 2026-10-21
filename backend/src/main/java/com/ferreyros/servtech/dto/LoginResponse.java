package com.ferreyros.servtech.dto;

public class LoginResponse {
    private String token;
    private String rol;
    private Long expiracion;
    private UsuarioDto user;

    public LoginResponse() {}

    public LoginResponse(String token, String rol, Long expiracion, UsuarioDto user) {
        this.token = token;
        this.rol = rol;
        this.expiracion = expiracion;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Long getExpiracion() { return expiracion; }
    public void setExpiracion(Long expiracion) { this.expiracion = expiracion; }

    public UsuarioDto getUser() { return user; }
    public void setUser(UsuarioDto user) { this.user = user; }
}
