package com.hostal.api.dto;

import com.hostal.api.entity.Producto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para Producto - transferencia de datos de productos/habitaciones
 */
public class ProductoDto {
    
    /**
     * DTO para mostrar información de producto
     */
    public static class ProductoResponse {
        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String categoria;
        private String imagenUrl;
        private Boolean activo;
        private Integer capacidadPersonas;
        private Integer metrosCuadrados;
        private String tipoCama;
        private Boolean tieneBanoPrivado;
        private Boolean tieneWifi;
        private Boolean tieneAireAcondicionado;
        private Boolean incluyeDesayuno;
        private Double calificacionPromedio;
        private Integer totalReviews;
        
        // Constructors
        public ProductoResponse() {}
        
        public ProductoResponse(Producto producto) {
            this.id = producto.getId();
            this.nombre = producto.getNombre();
            this.descripcion = producto.getDescripcion();
            this.precio = producto.getPrecio();
            this.stock = producto.getStock();
            this.categoria = producto.getCategoria().getDisplayName();
            this.imagenUrl = producto.getImagenUrl();
            this.activo = producto.getActivo();
            this.capacidadPersonas = producto.getCapacidadPersonas();
            this.metrosCuadrados = producto.getMetrosCuadrados();
            this.tipoCama = producto.getTipoCama();
            this.tieneBanoPrivado = producto.getTieneBanoPrivado();
            this.tieneWifi = producto.getTieneWifi();
            this.tieneAireAcondicionado = producto.getTieneAireAcondicionado();
            this.incluyeDesayuno = producto.getIncluyeDesayuno();
            this.calificacionPromedio = producto.getCalificacionPromedio();
            this.totalReviews = producto.getTotalReviews();
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
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
        
        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
        
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
        
        public Double getCalificacionPromedio() { return calificacionPromedio; }
        public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
        
        public Integer getTotalReviews() { return totalReviews; }
        public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    }
    
    /**
     * DTO para crear/actualizar producto
     */
    public static class ProductoRequest {
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
        private String nombre;
        
        @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
        private String descripcion;
        
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal precio;
        
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock = 0;
        
        @NotNull(message = "La categoría es obligatoria")
        private String categoria;
        
        private String imagenUrl;
        private Integer capacidadPersonas;
        private Integer metrosCuadrados;
        private String tipoCama;
        private Boolean tieneBanoPrivado = false;
        private Boolean tieneWifi = true;
        private Boolean tieneAireAcondicionado = false;
        private Boolean incluyeDesayuno = false;
        
        // Constructors
        public ProductoRequest() {}
        
        // Getters and Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        
        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }
        
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
        
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
    }
}