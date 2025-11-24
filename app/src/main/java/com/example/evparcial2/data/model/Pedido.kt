package com.example.evparcial2.data.model

data class Pedido(
    val id: Long,
    val productos: List<Producto>,
    val total: Double,
    val fecha: Long,
    val estado: String
)