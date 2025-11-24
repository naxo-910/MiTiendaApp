package com.example.evparcial2.data.model

data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val tipo: String,
    val ciudad: String,
    val pais: String,
    val imagenUrl: String? = null
)