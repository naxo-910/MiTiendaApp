package com.example.evparcial2.data.model

data class EntidadPedido(
    val id: Long,
    val productos: List<Producto>,
    val total: Double,
    val estado: String,
    val fecha: String,
    val fechaPedido: String = fecha,
    val productosJson: String = ""
)

// Nota: EntidadPedido es el modelo original de base de datos
// Pedido se define en ModelsExtended.kt para microservicios