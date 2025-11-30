package com.example.evparcial2.data.model

enum class TipoMensaje {
    ENVIADO, RECIBIDO
}

data class EntidadMensaje(
    val id: Long,
    val contenido: String,
    val fecha: String,
    val emisorId: Long,
    val emisorNombre: String,
    val tipo: TipoMensaje
)

data class EntidadChat(
    val id: Long,
    val nombreProducto: String,
    val ultimoMensaje: String,
    val fechaUltimoMensaje: String,
    val participanteId: Long,
    val participanteNombre: String
)