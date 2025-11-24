package com.example.evparcial2.data.local.entities

data class EntidadPedido(
    val id: Long = 0,
    val usuarioId: Long,
    val fechaPedido: Long = System.currentTimeMillis(),
    val total: Double,
    val estado: String,
    val direccionEntrega: String,
    val metodoPago: String = "tarjeta",
    val productosJson: String = ""
)