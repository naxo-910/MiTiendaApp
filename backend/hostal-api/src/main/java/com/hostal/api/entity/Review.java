package com.hostal.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Entidad Review - Representa las reseñas/calificaciones de productos
 */
@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private Integer calificacion;
    
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
    @Column(length = 1000)
    private String comentario;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activa")
    private Boolean activa = true;
    
    // Campos denormalizados para mejorar rendimiento
    @Column(name = "nombre_usuario", length = 100)
    private String nombreUsuario;
    
    // Constructors
    public Review() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Review(Producto producto, Usuario usuario, Integer calificacion) {
        this();
        this.producto = producto;
        this.usuario = usuario;
        this.calificacion = calificacion;
        this.nombreUsuario = usuario.getNombre();
    }
    
    public Review(Producto producto, Usuario usuario, Integer calificacion, String comentario) {
        this(producto, usuario, calificacion);
        this.comentario = comentario;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { 
        this.producto = producto; 
    }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
        if (usuario != null) {
            this.nombreUsuario = usuario.getNombre();
        }
    }
    
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    // Métodos de utilidad
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (usuario != null && nombreUsuario == null) {
            nombreUsuario = usuario.getNombre();
        }
    }
}