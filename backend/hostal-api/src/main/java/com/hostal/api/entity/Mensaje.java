package com.hostal.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Entidad Mensaje - Representa los mensajes individuales dentro de un chat
 */
@Entity
@Table(name = "mensajes")
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;
    
    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede exceder los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String contenido;
    
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @Column(name = "leido")
    private Boolean leido = false;
    
    @Column(name = "fecha_leido")
    private LocalDateTime fechaLeido;
    
    // Campo denormalizado para mejorar rendimiento
    @Column(name = "nombre_remitente", length = 100)
    private String nombreRemitente;
    
    // Constructors
    public Mensaje() {
        this.fechaEnvio = LocalDateTime.now();
    }
    
    public Mensaje(Chat chat, Usuario remitente, String contenido) {
        this();
        this.chat = chat;
        this.remitente = remitente;
        this.contenido = contenido;
        if (remitente != null) {
            this.nombreRemitente = remitente.getNombre();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }
    
    public Usuario getRemitente() { return remitente; }
    public void setRemitente(Usuario remitente) { 
        this.remitente = remitente; 
        if (remitente != null) {
            this.nombreRemitente = remitente.getNombre();
        }
    }
    
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    
    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { 
        this.leido = leido; 
        if (leido && fechaLeido == null) {
            this.fechaLeido = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getFechaLeido() { return fechaLeido; }
    public void setFechaLeido(LocalDateTime fechaLeido) { this.fechaLeido = fechaLeido; }
    
    public String getNombreRemitente() { return nombreRemitente; }
    public void setNombreRemitente(String nombreRemitente) { this.nombreRemitente = nombreRemitente; }
    
    // Métodos de utilidad
    @PrePersist
    public void prePersist() {
        if (fechaEnvio == null) {
            fechaEnvio = LocalDateTime.now();
        }
        if (remitente != null && nombreRemitente == null) {
            nombreRemitente = remitente.getNombre();
        }
    }
    
    public void marcarComoLeido() {
        this.leido = true;
        this.fechaLeido = LocalDateTime.now();
    }
}