package com.example.evparcial2.data.model

data class Chat(
    val id: Long = 0,
    val participantes: List<Long>, // IDs de usuarios
    val productoId: Long,
    val nombreProducto: String,
    val ultimoMensaje: String,
    val fechaUltimoMensaje: Long,
    val noLeidos: Int = 0
)

data class Mensaje(
    val id: Long = 0,
    val chatId: Long,
    val emisorId: Long,
    val emisorNombre: String,
    val contenido: String,
    val tipo: TipoMensaje = TipoMensaje.TEXTO,
    val fecha: Long = System.currentTimeMillis(),
    val leido: Boolean = false
)

enum class TipoMensaje {
    TEXTO,
    IMAGEN,
    UBICACION,
    CITA_PROPUESTA,
    CITA_CONFIRMADA,
    CITA_CANCELADA
}

data class PropuestaCita(
    val fechaPropuesta: Long,
    val direccion: String,
    val notas: String = ""
)