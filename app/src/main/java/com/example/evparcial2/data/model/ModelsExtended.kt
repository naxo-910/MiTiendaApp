package com.example.evparcial2.data.model

data class Review(
    val id: Long,
    val productoId: Long,
    val usuarioId: Long,
    val nombreUsuario: String,
    val comentario: String,
    val calificacion: Int, // 1-5
    val fechaCreacion: Long
)

data class Chat(
    val id: Long,
    val usuario1Id: Long,
    val usuario2Id: Long,
    val nombreUsuario1: String,
    val nombreUsuario2: String,
    val productoId: Long? = null, // Opcional: chat relacionado a un producto
    val fechaCreacion: Long,
    val ultimoMensaje: String? = null,
    val fechaUltimoMensaje: Long
)

data class Mensaje(
    val id: Long,
    val chatId: Long,
    val remitenteId: Long,
    val nombreRemitente: String,
    val contenido: String,
    val fechaEnvio: Long,
    val leido: Boolean = false
)

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
) {
    val subtotal: Double
        get() = producto.precio * cantidad
}

data class Pedido(
    val id: Long,
    val usuarioId: Long,
    val nombreUsuario: String,
    val emailUsuario: String,
    val items: List<ItemCarrito>,
    val total: Double,
    val estado: String, // "confirmado", "enviado", "entregado", "cancelado"
    val fechaCreacion: Long,
    val fechaActualizacion: Long,
    val direccionEntrega: String,
    val metodoPago: String
)