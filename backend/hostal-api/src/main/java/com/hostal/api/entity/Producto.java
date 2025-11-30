package com.hostal.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Producto - Representa las habitaciones/servicios del hostal
 */
@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaProducto categoria;
    
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Atributos específicos para habitaciones
    @Column(name = "capacidad_personas")
    private Integer capacidadPersonas;
    
    @Column(name = "metros_cuadrados")
    private Integer metrosCuadrados;
    
    @Column(name = "tipo_cama", length = 100)
    private String tipoCama;
    
    @Column(name = "tiene_bano_privado")
    private Boolean tieneBanoPrivado;
    
    @Column(name = "tiene_wifi")
    private Boolean tieneWifi = true;
    
    @Column(name = "tiene_aire_acondicionado")
    private Boolean tieneAireAcondicionado;
    
    @Column(name = "incluye_desayuno")
    private Boolean incluyeDesayuno;
    
    // Relación con reviews
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    // Constructors
    public Producto() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Producto(String nombre, String descripcion, BigDecimal precio, CategoriaProducto categoria) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public CategoriaProducto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProducto categoria) { this.categoria = categoria; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public Integer getCapacidadPersonas() { return capacidadPersonas; }
    public void setCapacidadPersonas(Integer capacidadPersonas) { this.capacidadPersonas = capacidadPersonas; }
    
    public Integer getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(Integer metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }
    
    public String getTipoCama() { return tipoCama; }
    public void setTipoCama(String tipoCama) { this.tipoCama = tipoCama; }
    
    public Boolean getTieneBanoPrivado() { return tieneBanoPrivado; }
    public void setTieneBanoPrivado(Boolean tieneBanoPrivado) { this.tieneBanoPrivado = tieneBanoPrivado; }
    
    public Boolean getTieneWifi() { return tieneWifi; }
    public void setTieneWifi(Boolean tieneWifi) { this.tieneWifi = tieneWifi; }
    
    public Boolean getTieneAireAcondicionado() { return tieneAireAcondicionado; }
    public void setTieneAireAcondicionado(Boolean tieneAireAcondicionado) { this.tieneAireAcondicionado = tieneAireAcondicionado; }
    
    public Boolean getIncluyeDesayuno() { return incluyeDesayuno; }
    public void setIncluyeDesayuno(Boolean incluyeDesayuno) { this.incluyeDesayuno = incluyeDesayuno; }
    
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    
    // Métodos de utilidad
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public double getCalificacionPromedio() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getCalificacion)
                .average()
                .orElse(0.0);
    }
    
    public int getTotalReviews() {
        return reviews != null ? reviews.size() : 0;
    }
    
    // Enum para categorías de producto
    public enum CategoriaProducto {
        HABITACION_INDIVIDUAL("Habitación Individual"),
        HABITACION_DOBLE("Habitación Doble"),
        HABITACION_FAMILIAR("Habitación Familiar"),
        DORMITORIO_COMPARTIDO("Dormitorio Compartido"),
        SUITE("Suite"),
        SERVICIO_ADICIONAL("Servicio Adicional");
        
        private final String displayName;
        
        CategoriaProducto(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}