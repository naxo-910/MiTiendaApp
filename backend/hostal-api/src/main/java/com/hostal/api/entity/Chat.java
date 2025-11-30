package com.hostal.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Chat - Representa las conversaciones entre usuarios
 */
@Entity
@Table(name = "chats")
public class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario1_id", nullable = false)
    private Usuario usuario1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario2_id", nullable = false)
    private Usuario usuario2;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto; // Chat relacionado con un producto específico (opcional)
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("fechaEnvio ASC")
    private List<Mensaje> mensajes;
    
    // Campos denormalizados para mejorar rendimiento
    @Column(name = "nombre_usuario1", length = 100)
    private String nombreUsuario1;
    
    @Column(name = "nombre_usuario2", length = 100)
    private String nombreUsuario2;
    
    @Column(name = "ultimo_mensaje", length = 500)
    private String ultimoMensaje;
    
    @Column(name = "fecha_ultimo_mensaje")
    private LocalDateTime fechaUltimoMensaje;
    
    // Constructors
    public Chat() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Chat(Usuario usuario1, Usuario usuario2) {
        this();
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        if (usuario1 != null) {
            this.nombreUsuario1 = usuario1.getNombre();
        }
        if (usuario2 != null) {
            this.nombreUsuario2 = usuario2.getNombre();
        }
    }
    
    public Chat(Usuario usuario1, Usuario usuario2, Producto producto) {
        this(usuario1, usuario2);
        this.producto = producto;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario1() { return usuario1; }
    public void setUsuario1(Usuario usuario1) { 
        this.usuario1 = usuario1; 
        if (usuario1 != null) {
            this.nombreUsuario1 = usuario1.getNombre();
        }
    }
    
    public Usuario getUsuario2() { return usuario2; }
    public void setUsuario2(Usuario usuario2) { 
        this.usuario2 = usuario2; 
        if (usuario2 != null) {
            this.nombreUsuario2 = usuario2.getNombre();
        }
    }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public List<Mensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }
    
    public String getNombreUsuario1() { return nombreUsuario1; }
    public void setNombreUsuario1(String nombreUsuario1) { this.nombreUsuario1 = nombreUsuario1; }
    
    public String getNombreUsuario2() { return nombreUsuario2; }
    public void setNombreUsuario2(String nombreUsuario2) { this.nombreUsuario2 = nombreUsuario2; }
    
    public String getUltimoMensaje() { return ultimoMensaje; }
    public void setUltimoMensaje(String ultimoMensaje) { this.ultimoMensaje = ultimoMensaje; }
    
    public LocalDateTime getFechaUltimoMensaje() { return fechaUltimoMensaje; }
    public void setFechaUltimoMensaje(LocalDateTime fechaUltimoMensaje) { this.fechaUltimoMensaje = fechaUltimoMensaje; }
    
    // Métodos de utilidad
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (usuario1 != null && nombreUsuario1 == null) {
            nombreUsuario1 = usuario1.getNombre();
        }
        if (usuario2 != null && nombreUsuario2 == null) {
            nombreUsuario2 = usuario2.getNombre();
        }
    }
    
    public void actualizarUltimoMensaje(String contenido, LocalDateTime fechaEnvio) {
        this.ultimoMensaje = contenido;
        this.fechaUltimoMensaje = fechaEnvio;
    }
    
    public Usuario getOtroUsuario(Long usuarioId) {
        if (usuario1 != null && usuario1.getId().equals(usuarioId)) {
            return usuario2;
        } else if (usuario2 != null && usuario2.getId().equals(usuarioId)) {
            return usuario1;
        }
        return null;
    }
    
    public String getNombreOtroUsuario(Long usuarioId) {
        if (usuario1 != null && usuario1.getId().equals(usuarioId)) {
            return nombreUsuario2;
        } else if (usuario2 != null && usuario2.getId().equals(usuarioId)) {
            return nombreUsuario1;
        }
        return "Usuario desconocido";
    }
}