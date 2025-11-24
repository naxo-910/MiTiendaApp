package com.example.evparcial2.data.local.entities

data class EntidadProducto(
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val tipo: String = "venta",
    val imagenUrl: String? = null,
    val disponible: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis()
)