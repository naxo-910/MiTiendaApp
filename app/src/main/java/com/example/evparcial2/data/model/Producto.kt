package com.example.evparcial2.data.model

data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val tipo: String,
    val ciudad: String = "",
    val pais: String = "",
    val imagenUrl: String? = null,
    
    // Campos adicionales para compatibilidad
    val promedioCalificacion: Float = 4.5f,
    val totalReviews: Int = 0
)