package com.hostal.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Pedido - Representa las reservas/pedidos de los usuarios
 */
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> items;
    
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    @Column(name = "direccion_entrega", length = 500)
    private String direccionEntrega;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private MetodoPago metodoPago;
    
    @Column(name = "notas", length = 1000)
    private String notas;
    
    // Campos denormalizados para mejorar rendimiento
    @Column(name = "nombre_usuario", length = 100)
    private String nombreUsuario;
    
    @Column(name = "email_usuario", length = 150)
    private String emailUsuario;
    
    // Fechas específicas para reservas de hostal
    @Column(name = "fecha_inicio_reserva")
    private LocalDateTime fechaInicioReserva;
    
    @Column(name = "fecha_fin_reserva")
    private LocalDateTime fechaFinReserva;
    
    @Column(name = "numero_huespedes")
    private Integer numeroHuespedes;
    
    // Constructors
    public Pedido() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Pedido(Usuario usuario, BigDecimal total, MetodoPago metodoPago) {
        this();
        this.usuario = usuario;
        this.total = total;
        this.metodoPago = metodoPago;
        if (usuario != null) {
            this.nombreUsuario = usuario.getNombre();
            this.emailUsuario = usuario.getEmail();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario;
        if (usuario != null) {
            this.nombreUsuario = usuario.getNombre();
            this.emailUsuario = usuario.getEmail();
        }
    }
    
    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    
    public LocalDateTime getFechaInicioReserva() { return fechaInicioReserva; }
    public void setFechaInicioReserva(LocalDateTime fechaInicioReserva) { this.fechaInicioReserva = fechaInicioReserva; }
    
    public LocalDateTime getFechaFinReserva() { return fechaFinReserva; }
    public void setFechaFinReserva(LocalDateTime fechaFinReserva) { this.fechaFinReserva = fechaFinReserva; }
    
    public Integer getNumeroHuespedes() { return numeroHuespedes; }
    public void setNumeroHuespedes(Integer numeroHuespedes) { this.numeroHuespedes = numeroHuespedes; }
    
    // Métodos de utilidad
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (fechaActualizacion == null) {
            fechaActualizacion = LocalDateTime.now();
        }
        if (usuario != null) {
            if (nombreUsuario == null) {
                nombreUsuario = usuario.getNombre();
            }
            if (emailUsuario == null) {
                emailUsuario = usuario.getEmail();
            }
        }
    }
    
    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Enums
    public enum EstadoPedido {
        PENDIENTE("Pendiente"),
        CONFIRMADO("Confirmado"),
        PROCESANDO("Procesando"),
        ENVIADO("Enviado"),
        ENTREGADO("Entregado"),
        CANCELADO("Cancelado"),
        REEMBOLSADO("Reembolsado");
        
        private final String displayName;
        
        EstadoPedido(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum MetodoPago {
        EFECTIVO("Efectivo"),
        TARJETA_CREDITO("Tarjeta de Crédito"),
        TARJETA_DEBITO("Tarjeta de Débito"),
        TRANSFERENCIA_BANCARIA("Transferencia Bancaria"),
        PAYPAL("PayPal"),
        PAGO_MOVIL("Pago Móvil");
        
        private final String displayName;
        
        MetodoPago(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}