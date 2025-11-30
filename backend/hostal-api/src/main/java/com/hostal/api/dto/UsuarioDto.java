package com.hostal.api.dto;

import com.hostal.api.entity.Usuario;

/**
 * DTO para Usuario - transferencia de datos sin información sensible
 */
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private Boolean activo;
    
    // Constructors
    public UsuarioDto() {}
    
    public UsuarioDto(Long id, String nombre, String email, String rol, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }
    
    // Constructor desde entidad
    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol().name();
        this.activo = usuario.getActivo();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}